package ma.youcode.api.services.implementations;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.enums.ShipmentStatus;
import ma.youcode.api.events.OnShipmentDeletedSuccessEvent;
import ma.youcode.api.exceptions.InvalidShipmentStateException;
import ma.youcode.api.exceptions.UnauthorizedShipmentAccessException;
import ma.youcode.api.models.Shipment;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.User;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.payloads.requests.ShipmentRequest;
import ma.youcode.api.payloads.responses.ShipmentResponse;
import ma.youcode.api.repositories.ShipmentRepository;
import ma.youcode.api.services.ImageService;
import ma.youcode.api.services.ShipmentService;
import ma.youcode.api.services.UserService;
import ma.youcode.api.utilities.PricingCalculator;
import ma.youcode.api.utilities.mappers.ShipmentMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starter.utilities.mappers.GenericMapper;
import org.starter.utilities.repositories.GenericRepository;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private static final Logger log = LogManager.getLogger(ShipmentServiceImpl.class);
    private final ShipmentRepository shipmentRepository;
    private final ImageService imageService;
    private final ShipmentMapper shipmentMapper;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;


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
        attachItems(shipment);

        calculateDistance(shipment);
        calculateShipmentPrice(shipment);
        return shipmentMapper.toResponseDTO(shipmentRepository.save(shipment));
    }

    @Override
    @Transactional
    public ShipmentResponse update(UUID shipmentId, ShipmentRequest request) {
        return findAndExecute(shipmentId, shipment -> {
            verifyAccess(shipment, "update");
            detachItems(shipment);
            shipmentMapper.updateEntity(request, shipment);
            attachItems(shipment);
            calculateDistance(shipment);
            calculateShipmentPrice(shipment);
            return shipmentMapper.toResponseDTO(shipmentRepository.save(shipment));
        });
    }

    @Override
    public void delete(UUID shipmentId) {
        findAndExecute(shipmentId, shipment -> {
            verifyAccess(shipment, "delete");
            shipmentRepository.delete(shipment);
            OnShipmentDeletedSuccessEvent event = new OnShipmentDeletedSuccessEvent(this, shipment);
            eventPublisher.publishEvent(event);
        });
    }

    @Override
    public Page<ShipmentResponse> loadCustomerShipments(Pageable pageable) {
        UUID customerId = getAuthUser().getId();
        Page<Shipment> shipments = shipmentRepository.findAllByCustomerId(pageable, customerId);
        return shipments.map(shipmentMapper::toResponseDTO);
    }


    private void detachItems(Shipment shipment) {
        shipment.getShipmentItems().forEach(item -> {
            log.info("Image URL {}", item.getImageURL());
            imageService.delete(item.getImageURL());
        });
        shipment.getShipmentItems().removeAll(shipment.getShipmentItems());
    }


    private void calculateShipmentPrice(Shipment shipment) {
        double totalVolume = calculateTotalVolume(shipment);
        shipment.setPrice(PricingCalculator.calculatePrice(totalVolume, shipment.getDistance()));
    }

    private void attachItems(Shipment shipment) {
        shipment.getShipmentItems().forEach(item -> {
            item.setShipment(shipment);
            item.setImageURL(imageService.uploadImage(item.getImage()));
            item.calculateVolume();
        });
    }

    private double calculateTotalVolume(Shipment shipment ) {
        return shipment.getShipmentItems().stream()
                .mapToDouble(shipmentItem -> {
                    shipmentItem.calculateVolume();
                    shipmentItem.setShipment(shipment);
                    return shipmentItem.getVolume();
                })
                .sum();
    }

    private UserSecurity getAuthUser() {
        return (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean isShipmentOwner(Shipment shipment) {
        return Objects.equals(shipment.getCustomer().getId(), getAuthUser().getId());
    }

    private void verifyAccess(Shipment shipment, String action) {
        if (!isShipmentOwner(shipment)) {
            throw new UnauthorizedShipmentAccessException(String.format("You don't have permission to %s this shipment", action));
        }
        if (!isShipmentPending(shipment)) {
            throw new InvalidShipmentStateException(String.format("You can't %s this shipment", action));
        }
    }

    private boolean isShipmentPending(Shipment shipment) {
        return shipment.getShipmentStatus().equals(ShipmentStatus.PENDING);
    }

    private void calculateDistance(Shipment shipment) {
        final int R = 6371;
        double latDistance = Math.toRadians(shipment.getArrival().lat() - shipment.getDeparture().lat());
        double lonDistance = Math.toRadians(shipment.getArrival().lon() - shipment.getDeparture().lon());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians( shipment.getDeparture().lat())) * Math.cos(Math.toRadians(shipment.getArrival().lat()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        shipment.setDistance(R * c);
    }
}
