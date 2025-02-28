package ma.youcode.api.payloads.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record AuthResponse(
        JwtResponse jwt,
        UserResponse user
) {
}
