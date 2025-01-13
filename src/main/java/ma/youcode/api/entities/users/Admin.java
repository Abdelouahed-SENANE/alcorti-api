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
@DiscriminatorValue("ROLE_ADMIN")
@Table(name = "ADMINS")
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends User {
    public Admin(User user) {
        super(user); // Call the User constructor that takes a User object
    }
}
