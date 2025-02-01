package ma.youcode.api.payloads.requests;

import jakarta.validation.constraints.*;
import org.starter.utilities.markers.validation.OnCreate;

public record AuthRequest(

        @Pattern(regexp = "^[A-Z]{1,2}\\d{4,10}$", message = "CIN must be 1-2 letters followed by 6-10 digits.")
        String cin,
        @Email( message = "Invalid email format.")
        String email,
        @NotBlank
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password
) {}
