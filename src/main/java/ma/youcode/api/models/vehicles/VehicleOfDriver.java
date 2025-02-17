package ma.youcode.api.models.vehicles;


import jakarta.persistence.*;
import lombok.*;
import ma.youcode.api.models.users.Driver;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles_of_drivers")
@Builder
public class VehicleOfDriver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID vehicleOfDriverId;


    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "image_url")
    private String imageUrl;

    @Transient
    private MultipartFile image;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
