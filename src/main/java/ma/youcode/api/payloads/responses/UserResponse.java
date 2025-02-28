package ma.youcode.api.payloads.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ma.youcode.api.enums.RoleType;
import ma.youcode.api.utilities.shared.Location;

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
        Location Location,
        String phoneNumber,
        Boolean active,
        Boolean emailVerified,
        Boolean isProfileCompleted,
        RoleType role
) {
}
