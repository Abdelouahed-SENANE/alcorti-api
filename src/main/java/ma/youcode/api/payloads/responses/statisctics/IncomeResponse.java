package ma.youcode.api.payloads.responses.statisctics;

import lombok.Builder;

import java.util.List;

@Builder
public record IncomeResponse(
    List<ChartItem> weekly,
    List<ChartItem> monthly
) {
}
