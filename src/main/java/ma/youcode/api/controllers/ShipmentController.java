package ma.youcode.api.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.payloads.requests.ShipmentRequest;
import ma.youcode.api.payloads.requests.ShipmentSearchCriteria;
import ma.youcode.api.payloads.responses.ShipmentResponse;
import ma.youcode.api.services.ShipmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.starter.utilities.dtos.SimpleSuccessDTO;
import org.starter.utilities.markers.validation.OnCreate;
import org.starter.utilities.markers.validation.OnUpdate;

import java.time.Instant;
import java.util.UUID;

import static org.starter.utilities.response.Response.simpleSuccess;

@RestController
@RequestMapping("api/v1/shipments")
@RequiredArgsConstructor
@Validated
public class ShipmentController {

    private static final Logger log = LogManager.getLogger(ShipmentController.class);
    private final ShipmentService shipmentService;
    private static  final String DEFAULT_PAGE = "0";
    private static  final String DEFAULT_SIZE = "10";

    @GetMapping("/all/available")
    public ResponseEntity<SimpleSuccessDTO> readAllAvailableShipments(@Valid @Min(1) @RequestParam(value = "page", defaultValue = DEFAULT_PAGE) int page,
                                                                      @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size,
                                                                      @RequestParam(value = "arrivalName" ,required = false) String arrivalName,
                                                                      @RequestParam(value = "departureName" , required = false) String departureName,
                                                                      @RequestParam(value = "startTime" , required = false) Instant startTime,
                                                                      @RequestParam(value = "endTime" , required = false) Instant endTime
    ) {
        ShipmentSearchCriteria criteria = ShipmentSearchCriteria.builder()
                .arrivalName(arrivalName)
                .departureName(departureName)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        return simpleSuccess(HttpStatus.OK.value(), "Shipments Available successfully.", shipmentService.loadAllAvailableShipments(pageable , criteria));
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SimpleSuccessDTO> readAllShipments(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) int page, @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return simpleSuccess(HttpStatus.OK.value(), "Shipments fetched successfully.", shipmentService.readAll(pageable));
    }

    @GetMapping("/all/customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> readAllShipmentsForCustomer(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) int page, @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        return simpleSuccess(HttpStatus.OK.value(), "Shipments fetched successfully.", shipmentService.loadMyShipments(pageable));
    }
    @GetMapping("/all/offers")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> readAllOffers(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) int page, @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        return simpleSuccess(HttpStatus.OK.value(), "All offers.", shipmentService.loadOffers(pageable));
    }

    @GetMapping("/all/driver")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<SimpleSuccessDTO> readAllShipmentsForDriver(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) int page,
                                                                      @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size,
                                                                      @RequestParam(value = "status" ,required = false) @Valid String status
        ) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        ShipmentSearchCriteria criteria = ShipmentSearchCriteria.builder()
                .status(status)
                .build();
        return simpleSuccess(HttpStatus.OK.value(), "Shipments fetched successfully.", shipmentService.loadShipmentsByDriver(pageable , criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleSuccessDTO> readShipmentById(@PathVariable UUID id) {
        return simpleSuccess(HttpStatus.OK.value(), "Shipments fetched successfully.", shipmentService.readById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> createShipment(@ModelAttribute @Validated({OnCreate.class}) ShipmentRequest request) {
        ShipmentResponse response = shipmentService.create(request);
        return simpleSuccess(HttpStatus.CREATED.value(), "Shipment created successfully.", response);
    }

    @PatchMapping("{shipmentId}/apply")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<SimpleSuccessDTO> applyShipment(@PathVariable UUID shipmentId) {
        shipmentService.applyShipment(shipmentId);
        return simpleSuccess(HttpStatus.OK.value(), "Driver applied to shipment successfully.");
    }

    @PatchMapping("{shipmentId}/accept")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> acceptApplyShipment(@PathVariable UUID shipmentId) {
        shipmentService.acceptApplyShipment(shipmentId);
        return simpleSuccess(HttpStatus.OK.value(), "Customer accept apply successfully.");
    }

    @PatchMapping("{shipmentId}/undo-apply")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<SimpleSuccessDTO> undoApplyShipment(@PathVariable UUID shipmentId) {
        shipmentService.undoApplyShipment(shipmentId);
        return simpleSuccess(HttpStatus.OK.value(), "Driver undone apply shipment successfully.");
    }

    @PatchMapping("{shipmentId}/in-transit")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<SimpleSuccessDTO> shipmentInTransit(@PathVariable UUID shipmentId) {
        shipmentService.shipmentInTransit(shipmentId);
        return simpleSuccess(HttpStatus.OK.value(), "Shipment marked as in transit successfully.");
    }

        @PatchMapping("{shipmentId}/delivered")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity<SimpleSuccessDTO> deliveryShipment(@PathVariable UUID shipmentId) {
        shipmentService.deliveryShipment(shipmentId);
        return simpleSuccess(HttpStatus.OK.value(), "Shipment delivered successfully.");
    }

    @PatchMapping("{shipmentId}/cancel")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> cancelShipment(@PathVariable UUID shipmentId) {
        shipmentService.cancelShipment(shipmentId);
        return simpleSuccess(HttpStatus.OK.value(), "Shipment cancelled by customer successfully.");
    }

    @PatchMapping("{shipmentId}/reject")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> rejectShipment(@PathVariable UUID shipmentId) {
        shipmentService.rejectApplyShipment(shipmentId);
        return simpleSuccess(HttpStatus.OK.value(), "Shipment rejected by customer successfully.");
    }

    @PutMapping("/{shipmentId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> updateShipment(@ModelAttribute @Validated({OnUpdate.class}) ShipmentRequest request, @PathVariable UUID shipmentId) {
        ShipmentResponse response = shipmentService.update(shipmentId, request);
        return simpleSuccess(201, "Shipment updated successfully.", response);
    }

    @DeleteMapping("/{shipmentId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> deleteShipment(@PathVariable UUID shipmentId) {
        shipmentService.delete(shipmentId);
        return simpleSuccess(HttpStatus.NO_CONTENT.value(), "Shipment deleted successfully.");
    }

}