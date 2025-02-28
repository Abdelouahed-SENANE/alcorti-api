package ma.youcode.api.services.implementations;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.enums.ShipmentStatus;
import ma.youcode.api.exceptions.DriverNotAllowedException;
import ma.youcode.api.exceptions.InvalidShipmentStateException;
import ma.youcode.api.exceptions.ResourceNotFoundException;
import ma.youcode.api.exceptions.UnauthorizedShipmentAccessException;
import ma.youcode.api.models.shipments.Shipment;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.Driver;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.payloads.requests.ShipmentRequest;
import ma.youcode.api.payloads.responses.ShipmentResponse;
import ma.youcode.api.repositories.ShipmentRepository;
import ma.youcode.api.services.ImageService;
import ma.youcode.api.services.ShipmentService;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentServiceImpl implements ShipmentService {

    private static final Logger log = LogManager.getLogger(ShipmentServiceImpl.class);
    private final ShipmentRepository shipmentRepository;
    private final ImageService imageService;
    private final ShipmentMapper shipmentMapper;


    /**
     * Retrieves the repository for the Shipment entity.
     * This repository is used by the service to interact with the database.
     *
     * @return the Shipment repository
     */
    @Override
    public GenericRepository<Shipment, UUID> getRepository() {
        return shipmentRepository;
    }

    /**
     * Retrieves the mapper for the Shipment entity.
     * This mapper is used by the service to map between the Shipment entity and its response/request DTOs.
     *
     * @return the Shipment mapper
     */
    @Override
    public GenericMapper<Shipment, ShipmentResponse, ShipmentRequest> getMapper() {
        return shipmentMapper;
    }

    /**
     * Creates a new shipment based on the provided request data.
     * The shipment is associated with the currently authenticated customer,
     * initialized with a PENDING status, and its items are attached.
     * The distance and price of the shipment are calculated before saving.
     *
     * @param request the shipment request data for creating the shipment
     * @return the created shipment response
     */
    @Override
    public ShipmentResponse create(ShipmentRequest request) {

        UUID customerId = getAuthUser().getId();
        Shipment shipment = shipmentMapper.fromRequestDTO(request);

        shipment.setCustomer(Customer.builder()
                .id(customerId)
                .build());

        shipment.setShipmentStatus(ShipmentStatus.PENDING);

        attachShipmentItems(shipment);
        calculateDistance(shipment);
        calculateShipmentPrice(shipment);


        return shipmentMapper.toResponseDTO(shipmentRepository.save(shipment));
    }

    /**
     * Updates the shipment with the given ID using the provided request data.
     * The function first verifies the action is allowed, detaches existing
     * shipment items, updates the shipment entity, reattaches the shipment
     * items, recalculates the distance and price, and then saves the updated shipment.
     *
     * @param shipmentId the ID of the shipment to update
     * @param request    the shipment request data for updating the shipment
     * @return the updated shipment response
     */
    @Override
    public ShipmentResponse update(UUID shipmentId, ShipmentRequest request) {
        return findAndExecute(shipmentId, shipment -> {
            hasPermission(shipment, "update");
            detachShipmentItems(shipment);
            shipmentMapper.updateEntity(request, shipment);
            attachShipmentItems(shipment);
            calculateDistance(shipment);
            calculateShipmentPrice(shipment);
            return shipmentMapper.toResponseDTO(shipmentRepository.save(shipment));
        });
    }

    /**
     * Deletes the shipment with the given ID.
     * The shipment status is first verified to be PENDING,
     * then the shipment is deleted and all associated shipment items are also deleted,
     * along with their images.
     *
     * @param shipmentId the ID of the shipment to delete
     */
    @Override
    public void delete(UUID shipmentId) {
        findAndExecute(shipmentId, shipment -> {
            hasPermission(shipment, "delete");
            shipmentRepository.delete(shipment);
            shipment.getShipmentItems().forEach(item -> {
                imageService.delete(item.getImageURL());
            });
        });
    }

    /**
     * Applies the currently authenticated driver to the specified shipment.
     * The shipment status is updated to APPLIED and the driver is assigned.
     *
     * @param shipmentId the ID of the shipment to apply to
     */
    @Override
    public void applyShipment(UUID shipmentId) {
        findAndExecute(shipmentId, shipment -> {
            verifyStatusTransition(shipment.getShipmentStatus(), ShipmentStatus.APPLIED);
            canApply(shipment);
            shipment.markAsApplied();
            shipment.setDriver(Driver.builder().id(getAuthUser().getId()).build());
            shipmentRepository.save(shipment);
        });
    }


    @Override
    public void undoApplyShipment(UUID shipmentId) {
        findAndExecute(shipmentId, shipment -> {
            verifyStatusTransition(shipment.getShipmentStatus(), ShipmentStatus.PENDING);
            canUnapply(shipment);
            shipment.markAsPending();
            shipment.setDriver(null);
            shipmentRepository.save(shipment);
        });
    }

    @Override
    public void shipmentInTransit(UUID shipmentId) {
        findAndExecute(shipmentId, shipment -> {
            verifyStatusTransition(shipment.getShipmentStatus(), ShipmentStatus.IN_TRANSIT);
            verifyDriver(shipment, "to in transit");
            shipment.markAsInTransit();
            shipmentRepository.save(shipment);
        });
    }

    @Override
    public void deliveryShipment(UUID shipmentId) {
        findAndExecute(shipmentId, shipment -> {
            verifyStatusTransition(shipment.getShipmentStatus(), ShipmentStatus.DELIVERED);
            verifyDriver(shipment, "to delivered");
            shipment.markAsDelivered();
            shipmentRepository.save(shipment);

        });
    }

    @Override
    public void rejectApplyShipment(UUID shipmentId) {
        findAndExecute(shipmentId, shipment -> {
            verifyStatusTransition(shipment.getShipmentStatus(), ShipmentStatus.PENDING);
            verifyOwnership(shipment, "to pending");
            shipment.markAsPending();
            shipment.setDriver(null);
            shipmentRepository.save(shipment);
        });
    }

    @Override
    public void cancelShipment(UUID shipmentId) {
        findAndExecute(shipmentId, shipment -> {
            verifyStatusTransition(shipment.getShipmentStatus(), ShipmentStatus.CANCELLED);
            verifyOwnership(shipment, "to cancelled");
            shipment.markAsCancelled();
            shipment.setDriver(null);
            shipmentRepository.save(shipment);
        });

    }


    @Override
    public Page<ShipmentResponse> loadMyShipments(Pageable pageable) {
        UUID customerId = getAuthUser().getId();
        Page<Shipment> shipments = shipmentRepository.findAllByCustomerId(pageable, customerId);
        return shipments.map(shipmentMapper::toResponseDTO);
    }

    /**
     * Retrieves all shipments for which the currently authenticated driver has applied.
     * The result is paginated according to the provided Pageable.
     *
     * @param pageable the pagination configuration
     * @return a Page of ShipmentResponse objects containing the shipments for which the driver has applied
     */
    @Override
    public Page<ShipmentResponse> loadShipmentsByDriver(Pageable pageable) {
        UUID driverId = getAuthUser().getId();
        Page<Shipment> shipments = shipmentRepository.findAllByDriverId(pageable, driverId);
        return shipments.map(shipmentMapper::toResponseDTO);
    }


    /**
     * Delete all images of shipment items and detach them from shipment.
     *
     * @param shipment shipment to detach items from.
     */
    private void detachShipmentItems(Shipment shipment) {
        shipment.getShipmentItems().forEach(item -> {
            log.info("Image URL {}", item.getImageURL());
            imageService.delete(item.getImageURL());
        });
        shipment.getShipmentItems().removeAll(shipment.getShipmentItems());
    }

    /**
     * Calculate the shipment price based on total volume and distance.
     *
     * @param shipment the shipment for which the price is calculated
     */
    private void calculateShipmentPrice(Shipment shipment) {
        double totalVolume = calculateTotalVolume(shipment);
        shipment.setPrice(PricingCalculator.calculatePrice(totalVolume, shipment.getDistance()));
    }

    /**
     * Attach shipment items to the shipment and upload images of items.
     * Each item is updated with the shipment and its image is uploaded.
     * The volume of each item is calculated.
     *
     * @param shipment the shipment to attach items to.
     */
    private void attachShipmentItems(Shipment shipment) {
        shipment.getShipmentItems().forEach(item -> {
            item.setShipment(shipment);
            item.setImageURL(imageService.uploadImage(item.getImage()));
            item.calculateVolume();
        });
    }

    private UserSecurity getAuthUser() {
        return (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean isOwnership(Shipment shipment) {
        return Objects.equals(shipment.getCustomer().getId(), getAuthUser().getId());
    }

    /**
     * Check if the shipment is applied by the currently authenticated driver.
     * <p>
     * The function checks if the shipment has a driver assigned and if the driver is the same as the
     * authenticated user.
     *
     * @param shipment the shipment to check
     * @return true if the shipment is applied by the authenticated driver, false otherwise
     */
    private boolean isAppliedDriver(Shipment shipment) {
        return shipment.getDriver().getId().equals(getAuthUser().getId());
    }

    private void canUnapply(Shipment shipment) {
        if (isPending(shipment)) {
            throw new InvalidShipmentStateException("You can't cancel this shipment because is not applied before.");
        }
        if (!isAppliedDriver(shipment)) {
            throw new DriverNotAllowedException("Only the applied driver can cancel the shipment.");
        }
    }

    private void hasPermission(Shipment shipment, String action) {
        if (!isOwnership(shipment)) {
            throw new UnauthorizedShipmentAccessException(String.format("You don't have permission to %s this shipment", action));
        }
        if (!isPending(shipment)) {
            throw new InvalidShipmentStateException(String.format("You can't %s this shipment", action));
        }
    }

    private void verifyOwnership(Shipment shipment, String action) {
        if (!isOwnership(shipment)) {
            throw new UnauthorizedShipmentAccessException(String.format("You don't have permission to %s this shipment", action));
        }
    }

    public void verifyDriver(Shipment shipment, String action) {
        if (!isAppliedDriver(shipment)) {
            throw new DriverNotAllowedException(String.format("Only the applied driver can mark the shipment as %s.", action));
        }
    }

    private void canApply(Shipment shipment) {
        if (!isPending(shipment)) {
            throw new InvalidShipmentStateException("You can't apply to this shipment");
        }

        if (Optional.ofNullable(shipment.getDriver()).isPresent()) {
            throw new InvalidShipmentStateException("This shipment is already applied");
        }
    }


    private boolean isPending(Shipment shipment) {
        return shipment.getShipmentStatus().equals(ShipmentStatus.PENDING);
    }

    private void calculateDistance(Shipment shipment) {
        final int R = 6371;
        double latDistance = Math.toRadians(shipment.getArrival().lat() - shipment.getDeparture().lat());
        double lonDistance = Math.toRadians(shipment.getArrival().lon() - shipment.getDeparture().lon());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(shipment.getDeparture().lat())) * Math.cos(Math.toRadians(shipment.getArrival().lat()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        shipment.setDistance(R * c);
    }

    private double calculateTotalVolume(Shipment shipment) {
        return shipment.getShipmentItems().stream()
                .mapToDouble(shipmentItem -> {
                    shipmentItem.calculateVolume();
                    shipmentItem.setShipment(shipment);
                    return shipmentItem.getVolume();
                })
                .sum();
    }

    private void verifyStatusTransition(ShipmentStatus currentStatus, ShipmentStatus newStatus) {
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new IllegalStateException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }

    private boolean isValidStatusTransition(ShipmentStatus currentStatus, ShipmentStatus newStatus) {
        return switch (currentStatus) {
            case PENDING -> newStatus == ShipmentStatus.APPLIED || newStatus == ShipmentStatus.CANCELLED;
            case APPLIED -> newStatus == ShipmentStatus.IN_TRANSIT || newStatus == ShipmentStatus.PENDING;
            case IN_TRANSIT -> newStatus == ShipmentStatus.DELIVERED || newStatus == ShipmentStatus.CANCELLED;
            case CANCELLED ->
                    newStatus == ShipmentStatus.PENDING || newStatus == ShipmentStatus.APPLIED || newStatus == ShipmentStatus.IN_TRANSIT;
            default -> false;
        };
    }

    @Override
    public Shipment findById(UUID id) {
        return shipmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Shipment not found."));
    }
}

