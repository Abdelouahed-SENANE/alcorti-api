package ma.youcode.api.payloads.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import ma.youcode.api.annotations.validation.FileGuard;
import ma.youcode.api.utilities.shared.Dimensions;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.markers.validation.OnCreate;
import org.starter.utilities.markers.validation.OnUpdate;

import java.util.UUID;

@Builder
public record ShipmentItemRequest(

        UUID shipmentItemId,
        @NotBlank(message = "Name is required")
        @Length(max = 120, message = "Name must be at most 150 characters")
        String name,
        @Valid
        Dimensions dimensions,
        @FileGuard(groups = {OnCreate.class , OnUpdate.class}, maxSize = 2)
        @Valid
        MultipartFile image,
        String imageURL

) {
}
