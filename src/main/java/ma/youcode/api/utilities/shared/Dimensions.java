package ma.youcode.api.utilities.shared;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.starter.utilities.markers.validation.OnCreate;

@Builder
@Embeddable
public record Dimensions(
        @NotNull(groups = OnCreate.class)
        @Min(value = 0, groups = OnCreate.class)
        Double length,
        @NotNull(groups = OnCreate.class)
        @Min(value = 0, groups = OnCreate.class)
        Double width,
        @Min(value = 0, groups = OnCreate.class)
        @NotNull(groups = OnCreate.class)
        Double height
) { }
