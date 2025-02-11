package ma.youcode.api.payloads.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ma.youcode.api.utilities.shared.Coordinates;
import org.starter.utilities.markers.validation.OnCreate;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostRequest(

        @NotBlank(groups = {OnCreate.class}, message = "Title is required")
        String title,

        @Valid
        Coordinates destination,

        @Valid
        Coordinates departure,

        @NotNull(groups = {OnCreate.class}, message = "Start time is required")
        LocalDateTime startTime,

        @NotNull(groups = {OnCreate.class}, message = "End time is required")
        LocalDateTime endTime,

        @NotNull(groups = {OnCreate.class}, message = "Distance is required")
        Integer distance,


        List<@Valid ArticleRequest> articles

) {
        @AssertTrue(groups = {OnCreate.class}, message = "Start time must be before end time")
        private boolean isValid() {
                return startTime.isBefore(endTime);
        }

}
