package ma.youcode.api.entities.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Entity
@Getter
@Setter
@DiscriminatorValue("ROLE_DRIVER")
@Table(name = "DRIVERS")
@AllArgsConstructor
public class Driver extends User {


}
