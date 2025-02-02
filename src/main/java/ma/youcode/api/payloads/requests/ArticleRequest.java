package ma.youcode.api.payloads.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ma.youcode.api.annotations.validation.FileGuard;
import ma.youcode.api.utilities.shared.Dimensions;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.markers.validation.OnCreate;

@Builder
public record ArticleRequest(

        @NotBlank
        @Length(max = 120, message = "Name must be at most 150 characters")
        String name,
        @Valid
        Dimensions dimensions,

        @NotNull
        @FileGuard(groups = {OnCreate.class}, maxSize = 2)
        @Valid
        MultipartFile image

) {
}
