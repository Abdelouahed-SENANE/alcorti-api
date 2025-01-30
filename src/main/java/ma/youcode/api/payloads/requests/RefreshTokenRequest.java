package ma.youcode.api.payloads.requests;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank
    String refreshToken
) {
}
