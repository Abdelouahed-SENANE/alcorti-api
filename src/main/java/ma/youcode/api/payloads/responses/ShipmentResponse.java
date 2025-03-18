package ma.youcode.api.payloads.responses;

import lombok.Builder;
import ma.youcode.api.enums.ShipmentStatus;
import ma.youcode.api.payloads.embedded.CustomerEmbedded;
import ma.youcode.api.payloads.embedded.DriverEmbedded;
import ma.youcode.api.utilities.shared.Location;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record ShipmentResponse(
    UUID id,
    String title,
    Location departure,
    Location arrival,
    double distance,
    double price,
    Instant startTime,
    Instant endTime,
    ShipmentStatus shipmentStatus,
    List<ShipmentItemResponse> items,
    CustomerEmbedded customer,
    DriverEmbedded driver,
    PaymentResponse payment
) {
}
