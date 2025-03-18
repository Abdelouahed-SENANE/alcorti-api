package ma.youcode.api.models;

import jakarta.persistence.*;
import lombok.*;
import ma.youcode.api.enums.PaymentStatus;
import ma.youcode.api.models.shipments.Shipment;
import org.starter.utilities.entities.Auditable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @OneToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

}
