package ma.youcode.api.services;


import ma.youcode.api.payloads.responses.ShipmentResponse;

import java.util.UUID;

public interface PaymentService {

    ShipmentResponse createPaymentIntent(UUID shipmentId);
    Boolean checkPaymentStatus(String paymentIntentId);
}
