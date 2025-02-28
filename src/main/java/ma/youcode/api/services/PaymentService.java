package ma.youcode.api.services;

import ma.youcode.api.payloads.requests.PaymentRequest;


public interface PaymentService {


    void createPayment(PaymentRequest request);

}
