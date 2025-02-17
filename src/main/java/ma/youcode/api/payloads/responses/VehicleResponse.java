package ma.youcode.api.payloads.responses;

import jakarta.validation.constraints.NotBlank;
import org.starter.utilities.markers.validation.OnCreate;

import java.util.UUID;

public record VehicleResponse(

        UUID vehicleId,
         String vehicleName,
        String vehicleType,
        String vehicleWeight,
        String vehicleManufacturer

) {
}
