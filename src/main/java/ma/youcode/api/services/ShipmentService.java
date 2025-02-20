package ma.youcode.api.services;

import ma.youcode.api.models.shipments.Shipment;
import ma.youcode.api.payloads.requests.ShipmentRequest;
import ma.youcode.api.payloads.responses.ShipmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.starter.utilities.services.crud.ReadAllService;
import org.starter.utilities.services.crud.ReadByIdService;
import org.starter.utilities.services.support.FindAndExecuteService;

import java.util.UUID;

public interface ShipmentService extends
        ReadAllService<ShipmentResponse, ShipmentRequest, Shipment, UUID>,
        ReadByIdService<ShipmentResponse, ShipmentRequest, Shipment, UUID>,
        FindAndExecuteService<ShipmentResponse, ShipmentRequest, Shipment, UUID> {

    Page<ShipmentResponse> loadMyShipments(Pageable pageable);
    Page<ShipmentResponse> loadShipmentsByDriver(Pageable pageable);

    void applyShipment(UUID shipmentId);
    void rejectApplyShipment(UUID shipmentId);
    void shipmentInTransit(UUID shipmentId);
    void deliveryShipment(UUID shipmentId);
    void cancelShipment(UUID shipmentId);
    void undoApplyShipment(UUID shipmentId);

    ShipmentResponse create(ShipmentRequest request);
    ShipmentResponse update(UUID shipmentId, ShipmentRequest request);
    Shipment findById(UUID id);
    void delete(UUID shipmentId);

}

