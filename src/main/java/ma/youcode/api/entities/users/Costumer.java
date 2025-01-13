package ma.youcode.api.entities.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder(toBuilder = true)
@Getter
@Setter
@DiscriminatorValue("ROLE_COSTUMER")
@Entity
@Table(name = "COSTUMERS")
@AllArgsConstructor
public class Costumer extends User{


}
