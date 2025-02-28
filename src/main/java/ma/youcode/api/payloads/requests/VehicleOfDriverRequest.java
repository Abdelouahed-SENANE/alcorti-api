package ma.youcode.api.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ma.youcode.api.annotations.validation.FileCheck;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.markers.validation.OnCreate;

import java.util.UUID;

public record VehicleOfDriverRequest(

        @NotNull(groups = OnCreate.class)
        UUID vehicleId,

        @FileCheck(groups = {OnCreate.class}, maxSize = 2, message = "Please upload a valid image for the vehicle (max size: 2MB)")
        MultipartFile image,

        @NotBlank(groups = OnCreate.class)
        String licensePlate

) {
}
