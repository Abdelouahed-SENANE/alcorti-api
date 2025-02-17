package ma.youcode.api.models;

import jakarta.persistence.*;
import lombok.*;
import ma.youcode.api.enums.PaymentMethod;
import ma.youcode.api.enums.PaymentStatus;
import ma.youcode.api.models.shipments.Shipment;
import org.starter.utilities.entities.Auditable;

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
    private double amount;

    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @ManyToOne
    @JoinColumn(name = "shipment_request_id")
    private Shipment shipment;

}
