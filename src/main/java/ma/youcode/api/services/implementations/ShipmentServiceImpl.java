package ma.youcode.api.services.implementations;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.enums.ShipmentStatus;
import ma.youcode.api.exceptions.ResourceNotFoundException;
import ma.youcode.api.exceptions.ShipmentAccessDeniedException;
import ma.youcode.api.models.Shipment;
import ma.youcode.api.models.ShipmentItem;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.User;
import ma.youcode.api.payloads.requests.ShipmentItemRequest;
import ma.youcode.api.payloads.requests.ShipmentRequest;
import ma.youcode.api.payloads.responses.ShipmentResponse;
import ma.youcode.api.repositories.ShipmentItemRepository;
import ma.youcode.api.repositories.ShipmentRepository;
import ma.youcode.api.services.ShipmentService;
import ma.youcode.api.services.UserService;
import ma.youcode.api.utilities.PricingCalculator;
import ma.youcode.api.utilities.mappers.ShipmentItemMapper;
import ma.youcode.api.utilities.mappers.ShipmentMapper;
import ma.youcode.api.utilities.shared.Coordinates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.starter.utilities.mappers.GenericMapper;
import org.starter.utilities.repositories.GenericRepository;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private static final Logger log = LogManager.getLogger(ShipmentServiceImpl.class);
    private final ShipmentRepository shipmentRepository;
    private final ShipmentItemRepository shipmentItemRepository;
    private final ShipmentMapper shipmentMapper;
    private final ShipmentItemMapper shipmentItemMapper;
    private final UserService userService;

    @Override
    public GenericRepository<Shipment, UUID> getRepository() {
        return shipmentRepository;
    }

    @Override
    public GenericMapper<Shipment, ShipmentResponse, ShipmentRequest> getMapper() {
        return shipmentMapper;
    }

    @Override
    public ShipmentResponse create(ShipmentRequest request) {

        UUID customerId = getAuthUser().getId();

        User customer = userService.findById(customerId);
        Shipment shipment = shipmentMapper.fromRequestDTO(request);

        shipment.setCustomer((Customer) customer);
        shipment.setShipmentStatus(ShipmentStatus.PENDING);
        shipment.setDistance(calculateDistance(request.departure(), request.arrival()));

        calculateShipmentPrice(shipment);
        return shipmentMapper.toResponseDTO(shipmentRepository.save(shipment));
    }

    @Override
    public ShipmentResponse update(UUID shipmentId, ShipmentRequest request) {
        return findAndExecute(shipmentId, shipment -> {
            verifyAccess(shipment, "update");
            shipmentMapper.updateEntity(request, shipment);
            Set<ShipmentItem> itemsToUpdate = identifyItemsToUpdate(shipment, request);
            Set<ShipmentItem> itemsToDelete = identifyItemsToDelete(shipment, request);
            Set<ShipmentItem> itemsToAdd = identifyItemsToAdd(request);

            updateItems(shipment, itemsToUpdate, request);
            deleteItems(shipment, itemsToDelete);
            addNewItems(shipment, itemsToAdd);

            calculateShipmentPrice(shipment);
            return shipmentMapper.toResponseDTO(shipmentRepository.save(shipment));
        });
    }

    @Override
    public void delete(UUID shipmentId) {
        findAndExecute(shipmentId, shipment -> {
            verifyAccess(shipment, "delete");
            shipmentRepository.delete(shipment);
        });
    }

    @Override
    public Page<ShipmentResponse> loadCustomerShipments(Pageable pageable) {
        UUID customerId = getAuthUser().getId();
        Page<Shipment> shipments = shipmentRepository.findAllByCustomerId(pageable, customerId);
        return shipments.map(shipmentMapper::toResponseDTO);
    }


    // Helper Methods
    private Set<UUID> extractItemIdsFromRequest(ShipmentRequest request) {
        return request.shipmentItems().stream()
                .map(ShipmentItemRequest::shipmentItemId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
    private Set<ShipmentItem> identifyItemsToUpdate(Shipment shipment, ShipmentRequest request) {

        Set<UUID> requestedItemIds = extractItemIdsFromRequest(request);
        return shipment.getShipmentItems().stream()
                .filter(item -> requestedItemIds.contains(item.getShipmentItemId()))
                .collect(Collectors.toSet());
    }

    private Set<ShipmentItem> identifyItemsToDelete(Shipment shipment, ShipmentRequest request) {
        Set<UUID> requestedItemIds = extractItemIdsFromRequest(request);
        return shipment.getShipmentItems().stream()
                .filter(item -> !requestedItemIds.contains(item.getShipmentItemId()))
                .collect(Collectors.toSet());
    }

    private Set<ShipmentItem> identifyItemsToAdd(ShipmentRequest request) {
        return request.shipmentItems().stream()
                .filter(dto -> dto.shipmentItemId() == null)
                .map(shipmentItemMapper::fromRequestDTO)
                .collect(Collectors.toSet());
    }

    private void updateItems(Shipment shipment, Set<ShipmentItem> itemsToUpdate, ShipmentRequest request) {
        itemsToUpdate.forEach(item -> {
            ShipmentItemRequest updateRequest = request.shipmentItems().stream()
                    .filter(dto -> dto.shipmentItemId().equals(item.getShipmentItemId()))
                    .findFirst()
                    .orElse(null);

            shipmentItemMapper.updateEntity(updateRequest, item);
            item.setShipment(shipment);
        });
    }

    private void deleteItems(Shipment shipment, Set<ShipmentItem> itemsToDelete) {
        shipment.getShipmentItems().removeAll(itemsToDelete);
        itemsToDelete.forEach(item -> item.setShipment(null));
    }

    private void addNewItems(Shipment shipment, Set<ShipmentItem> itemsToAdd) {
        shipment.getShipmentItems().addAll(itemsToAdd);
        itemsToAdd.forEach(item ->item.setShipment(shipment));
    }

    private void calculateShipmentPrice(Shipment shipment) {
        double totalVolume = shipment.getShipmentItems().stream()
                .mapToDouble(shipmentItem -> {
                    shipmentItem.calculateVolume();
                    shipmentItem.setShipment(shipment);
                    return shipmentItem.getVolume();
                })
                .sum();

        shipment.setPrice(PricingCalculator.calculatePrice(totalVolume, shipment.getDistance()));
    }


    private UserSecurity getAuthUser() {
        return (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean isShipmentOwner(Shipment shipment) {
        return Objects.equals(shipment.getCustomer().getId(), getAuthUser().getId());
    }

    private void verifyAccess(Shipment shipment, String action) {
        if (!isShipmentOwner(shipment)) {
            throw new ShipmentAccessDeniedException(String.format("You don't have permission to %s this post", action));
        }
    }

    private double calculateDistance(Coordinates departure, Coordinates arrival) {
        final int R = 6371;
        double latDistance = Math.toRadians(arrival.lat() - departure.lat());
        double lonDistance = Math.toRadians(arrival.lon() - departure.lon());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(departure.lat())) * Math.cos(Math.toRadians(arrival.lat()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
