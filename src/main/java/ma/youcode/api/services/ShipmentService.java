package ma.youcode.api.services;

import ma.youcode.api.models.Shipment;
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

    Page<ShipmentResponse> loadCustomerShipments(Pageable pageable);
    ShipmentResponse create(ShipmentRequest request);

    ShipmentResponse update(UUID shipmentId, ShipmentRequest request);

    void delete(UUID shipmentId);
}
