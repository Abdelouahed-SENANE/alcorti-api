package ma.youcode.api.models.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ma.youcode.api.utilities.shared.Coordinates;

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


}
