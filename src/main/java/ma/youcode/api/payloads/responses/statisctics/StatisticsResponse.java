package ma.youcode.api.payloads.responses.statisctics;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record StatisticsResponse(
        MetricsOverview usersMetrics,
        MetricsOverview activeUsersMetrics,
        MetricsOverview driversMetrics,
        MetricsOverview customersMetrics,
        MetricsOverview shipmentsMetrics,
        IncomeResponse income,
        List<ChartItem> countShipmentByStatus

) {
}
