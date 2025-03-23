package ma.youcode.api.payloads.responses.statisctics;

import lombok.Builder;

@Builder
public record MetricsOverview(
        long total,
        long previousTotal,
        double percentage
) {}
