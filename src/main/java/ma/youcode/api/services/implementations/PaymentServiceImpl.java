package ma.youcode.api.services.implementations;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.enums.PaymentStatus;
import ma.youcode.api.exceptions.DuplicatePaymentException;
import ma.youcode.api.exceptions.PaymentFailedException;
import ma.youcode.api.models.payments.CardPayment;
import ma.youcode.api.models.payments.CashPayment;
import ma.youcode.api.models.payments.Payment;
import ma.youcode.api.models.shipments.Shipment;
import ma.youcode.api.payloads.requests.PaymentRequest;
import ma.youcode.api.repositories.PaymentRepository;
import ma.youcode.api.services.ImageService;
import ma.youcode.api.services.PaymentService;
import ma.youcode.api.services.ShipmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LogManager.getLogger(PaymentServiceImpl.class);
    private final PaymentRepository paymentRepository;
    private final ShipmentService shipmentService;
    private final ImageService imageService;

    @Value("${app.stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }


    private void payUsingCard(CardPayment cardPayment) {

        double initialPrice = calculateInitialPrice(cardPayment.getShipment().getPrice());
        log.info("Initial price {}", initialPrice);
        cardPayment.setPaymentStatus(PaymentStatus.PROCESSING);
        cardPayment.setAmount(BigDecimal.valueOf(initialPrice));
        cardPayment.setTransactionId(null);
        cardPayment.setPaidAt(null);

        paymentRepository.save(cardPayment);

        try {
            Charge charge = createCharge(cardPayment);
            cardPayment.setPaymentStatus(PaymentStatus.SUCCEEDED);
            cardPayment.setPaidAt(LocalDateTime.now());
            cardPayment.setTransactionId(charge.getId());

        } catch (StripeException e) {
            cardPayment.setPaymentStatus(PaymentStatus.FAILED);
            log.error("Card payment failed for payment ID: {}", cardPayment.getPaymentId(), e);
            throw new PaymentFailedException(e.getMessage());
        }
        paymentRepository.save(cardPayment);

    }

    private Charge createCharge(CardPayment cardPayment) throws StripeException {
        final int CENT_PER_DOLLAR = 100;
        long amountInCents = Math.round(cardPayment.getAmount().doubleValue() * CENT_PER_DOLLAR);

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amountInCents);
        chargeParams.put("currency", "USD");
        chargeParams.put("source", "tok_visa");
        chargeParams.put("description", String.format("Shipment Payment %s", cardPayment.getShipment().getTitle()));
        return Charge.create(chargeParams);
    }

    private double calculateInitialPrice(double total) {
        double PAID_PERCENT = 0.1;
        return total * PAID_PERCENT;
    }

    private void payUsingCash(CashPayment cashPayment , MultipartFile document) {
        double initialPrice = calculateInitialPrice(cashPayment.getShipment().getPrice());

        String receiptUrl = imageService.uploadImage(document);
        cashPayment.setPaymentStatus(PaymentStatus.PROCESSING);
        cashPayment.setAmount(new BigDecimal(initialPrice));
        cashPayment.setReceiptUrl(receiptUrl);
        cashPayment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(cashPayment);
    }

    @Override
    public void createPayment(PaymentRequest request) {
        Shipment shipment = loadShipmentById(request.shipmentId());

        if (shipment.getPayment() != null && !shipment.getPayment().getPaymentStatus().equals(PaymentStatus.SUCCEEDED)) {
            throw new DuplicatePaymentException("Payment already processed.");
        }

        Payment payment;
        switch (request.method()) {
            case "CASH" -> {
                payment = new CashPayment();
                payment.setShipment(shipment);
                payUsingCash((CashPayment) payment, request.receipt());
            }
            case "CREDIT_CARD" -> {
                payment = new CardPayment();
                payment.setShipment(shipment);
                payUsingCard((CardPayment) payment);
            }
            default -> throw new PaymentFailedException("Payment method not supported.");
        }    }

    private Shipment loadShipmentById(UUID shipmentId) {
        return shipmentService.findById(shipmentId);
    }





}

