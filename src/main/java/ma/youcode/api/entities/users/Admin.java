package ma.youcode.api.entities.users;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Entity
@Getter
@Setter
@DiscriminatorValue("ROLE_ADMIN")
@Table(name = "ADMINS")
@AllArgsConstructor
public class Admin extends User {

}
