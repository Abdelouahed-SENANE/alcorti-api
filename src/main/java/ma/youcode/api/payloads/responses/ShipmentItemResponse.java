package ma.youcode.api.payloads.responses;

import ma.youcode.api.utilities.shared.Dimensions;

import java.util.UUID;

public record ShipmentItemResponse(
    UUID id,
    String name,
    Dimensions dimensions,
    double volume,
    String imageURL
) {
}
