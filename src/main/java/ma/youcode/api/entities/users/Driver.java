package ma.youcode.api.entities.users;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.youcode.api.utilities.shared.Coordinates;

@SuperBuilder(toBuilder = true)
@Entity
@Getter
@Setter
@DiscriminatorValue("ROLE_DRIVER")
@Table(name = "DRIVERS")
@AllArgsConstructor
@NoArgsConstructor
public class Driver extends User {

    @Column(name = "phone_number")
    private String phoneNumber;
    @Embedded
    private Coordinates coordinates;

}
