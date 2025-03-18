package ma.youcode.api.payloads.responses;

public record LocationResponse(
    double lat,
    double lon,
    String name
) {
}
