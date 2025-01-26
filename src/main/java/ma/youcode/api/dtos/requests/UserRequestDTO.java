package ma.youcode.api.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import ma.youcode.api.constants.UserType;
import ma.youcode.api.entities.users.User;
import ma.youcode.api.utilities.shared.Coordinates;
import org.starter.utilities.annotations.validation.Unique;
import org.starter.utilities.annotations.validation.ValidEnum;
import org.starter.utilities.markers.validation.OnCreate;

public record UserRequestDTO
        (
                @NotBlank(groups = OnCreate.class)
                @Unique(groups = OnCreate.class , entity = User.class , field = "cin" , message = "Cin already exists.")
                String cin,
                @NotBlank(groups = OnCreate.class)
                String firstName,
                @NotBlank(groups = OnCreate.class)
                String lastName,
                @NotBlank(groups = OnCreate.class)
                @Unique(groups = OnCreate.class , entity = User.class , field = "email" , message = "Email already exists.")
                String email,
                @NotBlank(groups = OnCreate.class)
//                @Min(value = 8 , message = "Password must be at least 8 characters long" , groups = OnCreate.class)
                String password,
                @NotBlank(groups = OnCreate.class)
                String picture,
                Coordinates coordinates,
                @NotBlank(groups = OnCreate.class)
                @Pattern(regexp = "^\\+212 \\d{9}$", message = "Invalid phone number format")
                String phoneNumber
        ) {
}
