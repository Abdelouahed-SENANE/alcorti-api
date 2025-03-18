package ma.youcode.api.payloads.embedded;

import com.fasterxml.jackson.annotation.JsonInclude;
import ma.youcode.api.utilities.shared.Location;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DriverEmbedded(
        UUID id,
        String cin,
        String firstName,
        String lastName,
        String email,
        String photoURL,
        Location location,
        String phoneNumber
) {
}
