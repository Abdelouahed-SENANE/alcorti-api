package ma.youcode.api.models;

import jakarta.persistence.*;
import lombok.*;
import ma.youcode.api.utilities.shared.Dimensions;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipment_items")
@Builder
@ToString
public class ShipmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shipmentItemId;

    @Column(name = "name")
    private String name;

    @Embedded
    private Dimensions dimensions;

    @Column(name = "volume")
    private double volume;

    @Column(name = "image_url")
    private String imageURL;

    @Transient
    private MultipartFile image;

    @ManyToOne
    @JoinColumn(name = "shipment_id" , nullable = false)
    private Shipment shipment;

    public void calculateVolume() {
        this.volume = dimensions.length() * dimensions.width() * dimensions.height();
    }

}
