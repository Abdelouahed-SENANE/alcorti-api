package ma.youcode.api.payloads.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ma.youcode.api.annotations.validation.FileCheck;
import ma.youcode.api.utilities.shared.Dimensions;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.markers.validation.OnCreate;
import org.starter.utilities.markers.validation.OnUpdate;

import java.util.UUID;

@Builder
public record ShipmentItemRequest(

        @NotBlank(message = "Name is required")
        @Length(max = 120, message = "Name must be at most 150 characters")
        String name,
        @Valid
        Dimensions dimensions,
        @NotNull(message = "Image is required" , groups = {OnCreate.class , OnUpdate.class})
        @FileCheck(groups = {OnCreate.class , OnUpdate.class}, maxSize = 2 , allowedTypes = {"image/webp"})
        MultipartFile image
) {
}
