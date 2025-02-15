package ma.youcode.api.payloads.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ma.youcode.api.enums.RoleType;
import ma.youcode.api.utilities.shared.Coordinates;

import java.util.UUID;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Builder
public record UserResponse(
        UUID id,
        String cin,
        String firstName,
        String lastName,
        String email,
        String photoURL,
        Coordinates coordinates,
        String phoneNumber,
        Boolean active,
        Boolean emailVerified,
        RoleType role
) {
}
