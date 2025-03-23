package ma.youcode.api.payloads.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ma.youcode.api.enums.PaymentStatus;
import ma.youcode.api.payloads.embedded.ShipmentEmbedded;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        Double amount,
        String transactionId,
        String paymentIntentId,

        PaymentStatus paymentStatus,
        ShipmentEmbedded shipment
) {
}
