package ma.youcode.api.payloads.requests;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import org.starter.utilities.markers.validation.OnCreate;

import java.util.UUID;

public record VehicleRequest(

        @NotBlank(groups = OnCreate.class)
         String vehicleName,
        @NotBlank(groups = OnCreate.class)
        String vehicleType,
        @NotBlank(groups = OnCreate.class)
        String vehicleWeight,
        @NotBlank(groups = OnCreate.class)
        String vehicleManufacturer

) {
}
