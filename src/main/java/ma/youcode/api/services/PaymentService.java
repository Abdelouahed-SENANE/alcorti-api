package ma.youcode.api.services;


import ma.youcode.api.payloads.responses.PaymentResponse;
import ma.youcode.api.payloads.responses.ShipmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    ShipmentResponse createPaymentIntent(UUID shipmentId);
    Boolean checkPaymentStatus(String paymentIntentId);
    Page<PaymentResponse> loadAllPayments(Pageable pageable);
}
