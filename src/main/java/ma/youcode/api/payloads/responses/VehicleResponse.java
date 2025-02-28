package ma.youcode.api.payloads.responses;

import java.util.UUID;

public record VehicleResponse(

        UUID vehicleId,
         String vehicleName,
        String vehicleType,
        String vehicleWeight,
        String vehicleManufacturer

) {
}
