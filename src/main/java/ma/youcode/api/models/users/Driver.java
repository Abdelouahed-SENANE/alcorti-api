package ma.youcode.api.models.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ma.youcode.api.models.shipments.Shipment;
import ma.youcode.api.models.vehicles.VehicleOfDriver;
import ma.youcode.api.utilities.shared.Coordinates;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("ROLE_DRIVER")
@Table(name = "DRIVERS")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Driver extends User {

    @Column(name = "phone_number")
    private String phoneNumber;
    @Embedded
    private Coordinates coordinates;

    @OneToMany(mappedBy = "driver" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Shipment> shipments = new HashSet<>();

    @OneToMany(mappedBy = "driver" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<VehicleOfDriver> vehiclesOfDriver = new HashSet<>();



}
