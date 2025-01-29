package ma.youcode.api.payload.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @NotBlank
        @Pattern(regexp = "^[A-Z]{1,2}\\d{4,10}$", message = "CIN must be 1-2 letters followed by 6-10 digits.")
        String cin,
        @NotBlank
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password
) {
}
