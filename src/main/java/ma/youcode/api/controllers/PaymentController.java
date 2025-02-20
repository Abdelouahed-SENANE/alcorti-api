package ma.youcode.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.payloads.requests.PaymentRequest;
import ma.youcode.api.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.starter.utilities.dtos.SimpleSuccessDTO;
import org.starter.utilities.markers.validation.OnCreate;

import static org.starter.utilities.response.Response.simpleSuccess;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<SimpleSuccessDTO> createPayment(@ModelAttribute @Validated({OnCreate.class}) PaymentRequest paymentRequest) {
        paymentService.createPayment(paymentRequest);
        return simpleSuccess(HttpStatus.OK.value() , "Shipment paid successfully.");
    }

}
