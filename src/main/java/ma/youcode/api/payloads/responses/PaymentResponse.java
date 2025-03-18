package ma.youcode.api.payloads.responses;

public record PaymentResponse(
        Double amount,
        String paymentIntentId
) {
}
