package ma.youcode.api.services.implementations;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.enums.PaymentStatus;
import ma.youcode.api.exceptions.PaymentProcessingException;
import ma.youcode.api.exceptions.ResourceNotFoundException;
import ma.youcode.api.models.Payment;
import ma.youcode.api.models.shipments.Shipment;
import ma.youcode.api.payloads.responses.PaymentResponse;
import ma.youcode.api.payloads.responses.ShipmentResponse;
import ma.youcode.api.repositories.PaymentRepository;
import ma.youcode.api.repositories.ShipmentRepository;
import ma.youcode.api.services.PaymentService;
import ma.youcode.api.services.ShipmentService;
import ma.youcode.api.utilities.mappers.PaymentMapper;
import ma.youcode.api.utilities.mappers.ShipmentMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LogManager.getLogger(PaymentServiceImpl.class);
    private final ShipmentService shipmentService;
    private final PaymentRepository paymentRepository;
    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private  final PaymentMapper paymentMapper;

    @Value("${app.stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @Override
    public Boolean checkPaymentStatus(String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            String status = paymentIntent.getStatus();

            String shipmentIdStr = paymentIntent.getMetadata().get("shipment_id");

            if (shipmentIdStr == null || shipmentIdStr.isEmpty()) {
                log.error("Shipment ID is missing in payment metadata");
                throw new ResourceNotFoundException("Shipment ID is missing in payment metadata");
            }

            UUID shipmentId = UUID.fromString(shipmentIdStr);

            return switch (status) {
                case "succeeded" -> {
                    markShipmentAsPaid(shipmentId, paymentIntentId);
                    yield true;
                }
                case "requires_payment_method", "failed" -> {
                    markShipmentAsUnpaid(shipmentId, paymentIntentId);
                    yield false;
                }
                case "processing" -> false;
                default -> {
                    log.info("Unhandled payment status: {}", status);
                    throw new PaymentProcessingException("Unhandled payment status: " + status);
                }
            };

        } catch (StripeException e) {
            log.error("Error checking payment status: {}", e.getMessage());
            throw new PaymentProcessingException("Error checking payment status: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Invalid shipment ID format: {}", e.getMessage());
            throw new PaymentProcessingException("Invalid shipment ID format: " + e.getMessage());
        }
    }

    private void markShipmentAsPaid(UUID shipmentId, String paymentIntentId) {
        Shipment shipment = loadShipmentById(shipmentId);
        shipment.getPayment().setPaymentStatus(PaymentStatus.SUCCEEDED);
        shipment.getPayment().setTransactionId(paymentIntentId);
        shipment.getPayment().setPaidAt(LocalDateTime.now());
        paymentRepository.save(shipment.getPayment());

    }

    private void markShipmentAsUnpaid(UUID shipmentId, String paymentIntentId) {
        Shipment shipment = loadShipmentById(shipmentId);
        shipment.getPayment().setPaymentStatus(PaymentStatus.FAILED);
        shipment.getPayment().setTransactionId(paymentIntentId);
        shipment.getPayment().setPaidAt(LocalDateTime.now());
        paymentRepository.save(shipment.getPayment());
    }

    public ShipmentResponse createPaymentIntent(UUID shipmentId) {
        try {
            Shipment shipment = loadShipmentById(shipmentId);
            long amount = calculatePaidAmount(shipment);

            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount);
            params.put("currency", "usd");
            params.put("payment_method_types", List.of("card"));
            params.put("description", "Payment for Shipment #" + shipment.getTitle());

            Map<String, String> metadata = new HashMap<>();

            metadata.put("shipment_id", shipment.getId().toString());
            metadata.put("shipment_title", shipment.getTitle());
            params.put("metadata", metadata);

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            Payment payment = Payment.builder()
                    .amount(amount / 100.0)
                    .paymentStatus(PaymentStatus.PROCESSING)
                    .shipment(shipment)
                    .paymentIntentId(paymentIntent.getClientSecret())
                    .paidAt(null)
                    .transactionId(null)
                    .build();
            shipment.setPayment(payment);

            return shipmentMapper.toResponseDTO(shipmentRepository.save(shipment));
        } catch (StripeException e) {
            throw new PaymentProcessingException("Failed to create payment intent", e);
        }

    }

    private long calculatePaidAmount(Shipment shipment) {
        final int CENT_PER_DOLLAR = 100;
        double PAID_PERCENT = 0.1;
        double paidAmount = shipment.getPrice() * PAID_PERCENT;
        return Math.round(paidAmount * CENT_PER_DOLLAR);
    }

    private Shipment loadShipmentById(UUID shipmentId) {
        return shipmentService.findById(shipmentId);
    }

    @Override
    public Page<PaymentResponse> loadAllPayments(Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.map(paymentMapper::toResponseDTO);
    }
}

