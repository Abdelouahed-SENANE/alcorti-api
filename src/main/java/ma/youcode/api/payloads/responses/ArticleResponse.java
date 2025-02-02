package ma.youcode.api.payloads.responses;

import ma.youcode.api.utilities.shared.Dimensions;

import java.util.UUID;

public record ArticleResponse(
    UUID id,
    Dimensions dimensions,
    double volume,
    String imageURL
) {
}
