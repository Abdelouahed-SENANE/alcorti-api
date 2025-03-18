package ma.youcode.api.utilities.shared;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.starter.utilities.markers.validation.OnCreate;

@Builder
@Embeddable
public record Location(

        @NotNull(groups = OnCreate.class)
        @DecimalMin(value = "-180.0", groups = OnCreate.class)
        @DecimalMax(value = "180.0", groups = OnCreate.class)
        double lon,
        @NotNull(groups = OnCreate.class)
        @DecimalMin(value = "-90.0", groups = OnCreate.class)
        @DecimalMax(value = "90.0", groups = OnCreate.class)
        double lat,
        @NotNull(groups = OnCreate.class)
        @Size(max = 255, groups = OnCreate.class)
        String name
) {
}
