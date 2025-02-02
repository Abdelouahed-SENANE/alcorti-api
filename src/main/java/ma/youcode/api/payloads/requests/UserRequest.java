package ma.youcode.api.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ma.youcode.api.annotations.validation.FileGuard;
import ma.youcode.api.models.users.User;
import ma.youcode.api.utilities.shared.Coordinates;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.annotations.validation.Unique;
import org.starter.utilities.markers.validation.OnCreate;
import org.starter.utilities.markers.validation.OnUpdate;

@Builder
public record UserRequest
        (
                @NotBlank(groups = OnCreate.class)
                @Unique(groups = OnCreate.class, entity = User.class, field = "cin", message = "CIN already exists.")
                @Pattern(regexp = "^[A-Z]{1,2}\\d{4,10}$", message = "CIN must be 1-2 letters followed by 6-10 digits.")
                String cin,
                @NotBlank(groups = OnCreate.class)
                String firstName,
                @NotBlank(groups = OnCreate.class)
                String lastName,
                @NotBlank(groups = OnCreate.class)
                @Unique(groups = OnCreate.class, entity = User.class, field = "email", message = "Email already exists.")
                @Email(groups = OnCreate.class, message = "Invalid email format.")
                String email,
                @NotBlank(groups = OnCreate.class)
                @Size(min = 8, message = "Password must be at least 8 characters long", groups = OnCreate.class)
                String password,
                @FileGuard(groups = {OnUpdate.class}, maxSize = 2)
                MultipartFile photo,
                Coordinates coordinates,
                @NotBlank(groups = OnCreate.class)
                @Pattern(regexp = "^(?:\\+?212\\s?|\\(?0\\)?)(\\d{9})$", message = "Invalid phone number format")
                String phoneNumber
        ) {
}
