package ma.youcode.api.payloads.responses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ma.youcode.api.annotations.validation.FileCheck;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.markers.validation.OnCreate;

import java.util.UUID;

public record VehicleOfDriverResponse(


        String image,

        String licensePlate

) {
}
