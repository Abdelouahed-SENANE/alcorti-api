package ma.youcode.api.models.vehicles;

import jakarta.persistence.*;
import lombok.*;
import org.starter.utilities.entities.Auditable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles")
@Builder
public class Vehicle extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID vehicleId;
    private String vehicleName;
    private String vehicleType;
    private String vehicleWeight;
    private String vehicleManufacturer;

    @OneToMany(mappedBy = "vehicle" , cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.LAZY)
    private Set<VehicleOfDriver> vehiclesOfDriver = new HashSet<>();

}
