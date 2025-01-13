package ma.youcode.api.entities.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Builder
@Getter
@Setter
@DiscriminatorValue("ROLE_COSTUMER")
@Entity
@Table(name = "COSTUMERS")
@AllArgsConstructor
@NoArgsConstructor
public class Costumer extends User{
    public Costumer(User user) {
        super(user); // Call the User constructor that takes a User object
    }

}
