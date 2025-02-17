package ma.youcode.api.models.shipments;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ma.youcode.api.enums.ShipmentStatus;
import ma.youcode.api.models.Payment;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.Driver;
import ma.youcode.api.utilities.shared.Coordinates;
import org.starter.utilities.entities.Auditable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipments")
@SuperBuilder(toBuilder = true)

public class Shipment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shipmentId;

    @Column(name = "title")
    private String title;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "departure_latitude")),
            @AttributeOverride(name = "lon", column = @Column(name = "departure_longitude"))
    })
    private Coordinates departure;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "arrival_latitude")),
            @AttributeOverride(name = "lon", column = @Column(name = "arrival_longitude"))
    })
    private Coordinates arrival;

    @Column(name = "distance")
    private double distance;

    @Column(name = "price")
    private double price;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "shipment_status")
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;

    @OneToMany(mappedBy = "shipment" , cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShipmentItem> shipmentItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @OneToMany(mappedBy = "shipment" , cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();


    public void markAsPending() {
        this.setShipmentStatus(ShipmentStatus.PENDING);
    }
    public void markAsInTransit() {
        this.setShipmentStatus(ShipmentStatus.IN_TRANSIT);
    }

    public void markAsDelivered() {
        this.setShipmentStatus(ShipmentStatus.DELIVERED);
    }

    public void markAsApplied() {
        this.setShipmentStatus(ShipmentStatus.APPLIED);
    }

    public void markAsCancelled() {
        this.setShipmentStatus(ShipmentStatus.CANCELLED);
    }

}
