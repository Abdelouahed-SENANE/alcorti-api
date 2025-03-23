package ma.youcode.api.payloads.embedded;

import ma.youcode.api.enums.ShipmentStatus;
import ma.youcode.api.payloads.responses.PaymentResponse;
import ma.youcode.api.payloads.responses.ShipmentItemResponse;
import ma.youcode.api.utilities.shared.Location;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ShipmentEmbedded(
        UUID id,
        String title,
        double price,
        CustomerEmbedded customer
) {
}
