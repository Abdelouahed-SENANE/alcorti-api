package ma.youcode.api.payloads.requests;

import lombok.Builder;
import ma.youcode.api.enums.ShipmentStatus;
import org.starter.utilities.annotations.validation.EnumCheck;

import java.time.Instant;

@Builder
public record ShipmentSearchCriteria(
        Instant startTime,
        Instant endTime,
        String title,
        String arrivalName,
        String departureName,
        @EnumCheck(enumClass = ShipmentStatus.class , message = "Invalid status" ) String status
) {
}
