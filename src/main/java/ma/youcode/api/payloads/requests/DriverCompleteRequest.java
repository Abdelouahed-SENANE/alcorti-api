package ma.youcode.api.payloads.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ma.youcode.api.utilities.shared.Location;

import java.util.List;

public record DriverCompleteRequest(

        @NotNull(message =  "Location must be not null.") @Valid Location location,
        @NotEmpty(message = "Vehicles list must not be empty.") List<@Valid  VehicleOfDriverRequest> vehicles
) {
}
