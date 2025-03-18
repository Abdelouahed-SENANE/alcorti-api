package ma.youcode.api.payloads.responses;

import java.util.UUID;

public record VehicleResponse(

        UUID id,
        String vehicleName,
        String vehicleType,
        String vehicleWeight,
        String vehicleManufacturer

) {
}
