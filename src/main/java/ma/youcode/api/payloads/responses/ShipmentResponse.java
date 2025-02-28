package ma.youcode.api.payloads.responses;

import ma.youcode.api.enums.ShipmentStatus;
import ma.youcode.api.payloads.embedded.CustomerEmbedded;
import ma.youcode.api.utilities.shared.Location;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record ShipmentResponse(
    UUID shipmentId,
    String title,
    Location departure,
    Location arrival,
    double distance,
    double price,
    LocalDateTime startTime,
    LocalDateTime endTime,
    ShipmentStatus shipmentStatus,
    Set<ShipmentItemResponse> shipmentItems,
    CustomerEmbedded customer
) {
}
