package ma.youcode.api.payloads.responses;

import lombok.Builder;

@Builder
public record JwtResponse(
        String accessToken,
        String refreshToken,
        Long expiresIn
) {
}
