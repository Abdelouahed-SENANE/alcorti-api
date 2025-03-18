package ma.youcode.api.payloads.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ma.youcode.api.utilities.shared.Location;
import org.starter.utilities.markers.validation.OnCreate;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Builder
public record ShipmentRequest(

        @NotBlank(groups = {OnCreate.class}, message = "Title is required")
        String title,

        @Valid
        Location arrival,

        @Valid
        Location departure,

        @NotNull(groups = {OnCreate.class}, message = "Start time is required")
        Instant startTime,

        @NotNull(groups = {OnCreate.class}, message = "End time is required")
        Instant endTime,

        List<@Valid ShipmentItemRequest> items


) {
        @AssertTrue(groups = {OnCreate.class}, message = "Start time must be before end time")
        public boolean isValid() {
                return startTime.isBefore(endTime);
        }

}
