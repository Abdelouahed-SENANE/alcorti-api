package ma.youcode.api.payloads.responses;

import ma.youcode.api.enums.PostStatus;
import ma.youcode.api.payloads.embedded.CustomerEmbedded;
import ma.youcode.api.utilities.shared.Coordinates;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record PostResponse(
    UUID id,
    String title,
    Coordinates departure,
    Coordinates destination,
    int distance,
    double price,
    LocalDateTime startTime,
    LocalDateTime endTime,
    PostStatus postStatus,
    Set<ArticleResponse> articles,
    CustomerEmbedded customer
) {
}
