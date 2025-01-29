package ma.youcode.api.payload.requests;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
        @NotBlank
    String refreshToken
) {
}
