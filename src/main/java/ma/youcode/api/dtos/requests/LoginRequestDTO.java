package ma.youcode.api.dtos.requests;

public record LoginRequestDTO(
    String cin,
    String password
) {
}
