package ma.youcode.api.controllers;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.payloads.requests.PaymentRequest;
import ma.youcode.api.payloads.responses.ShipmentResponse;
import ma.youcode.api.services.PaymentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.starter.utilities.dtos.SimpleSuccessDTO;
import org.starter.utilities.markers.validation.OnCreate;

import java.util.UUID;

import static org.starter.utilities.response.Response.simpleSuccess;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private static  final String DEFAULT_PAGE = "0";
    private static  final String DEFAULT_SIZE = "10";

    @PostMapping("/create-payment-intent/{id}")
    public ResponseEntity<SimpleSuccessDTO> generateSessionId(@PathVariable UUID id) {
        ShipmentResponse shipmentResponse = paymentService.createPaymentIntent(id);
        return simpleSuccess(HttpStatus.OK.value() , "Generate payment intent successfully." , shipmentResponse);
    }
    @PostMapping("/confirm-payment/{id}")
    public ResponseEntity<SimpleSuccessDTO> checkPaymentStatus(@PathVariable String id) {
        return simpleSuccess(HttpStatus.OK.value() , "Payment checked successfully." , paymentService.checkPaymentStatus(id));
    }

    @GetMapping("/all")
    public ResponseEntity<SimpleSuccessDTO> readAllPayment(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) int page, @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return simpleSuccess(HttpStatus.OK.value() , "Payment fetched successfully." , paymentService.loadAllPayments(pageable));
    }

}
