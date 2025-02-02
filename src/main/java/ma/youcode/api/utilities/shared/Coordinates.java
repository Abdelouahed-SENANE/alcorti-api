package ma.youcode.api.utilities.shared;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.starter.utilities.markers.validation.OnCreate;

@Builder
@Embeddable
public record Coordinates(
        @NotNull(groups = OnCreate.class) double lon,
        @NotNull(groups = OnCreate.class) double lat
) {
}
