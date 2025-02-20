package ma.youcode.api.services;

import ma.youcode.api.payloads.requests.PaymentRequest;

import java.util.UUID;


public interface PaymentService {


    void createPayment(PaymentRequest request);

}
