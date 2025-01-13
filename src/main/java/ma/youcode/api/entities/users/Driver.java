package ma.youcode.api.entities.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@DiscriminatorValue("ROLE_DRIVER")
@Table(name = "DRIVERS")
@AllArgsConstructor
@NoArgsConstructor
public class Driver extends User {
    public Driver(User user) {
        super(user); // Call the User constructor that takes a User object
    }


}
