package ma.youcode.api.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import ma.youcode.api.constants.RoleName;
import ma.youcode.api.utilities.shared.Coordinates;


import java.util.UUID;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record UserResponseDTO(
        UUID id,
        String cin,
        String firstName,
        String lastName,
        String email,
        String password,
        String picture,
        Coordinates coordinates,
        String phoneNumber,
        Boolean isActive,
        Boolean isEmailVerified,
        RoleName role

) {
}
