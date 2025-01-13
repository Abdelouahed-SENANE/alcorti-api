package ma.youcode.api.entities.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.starter.utilities.entities.BaseEntity;

import java.util.UUID;



@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "users" , uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"cin"})
})
@DiscriminatorColumn(name = "role_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User extends BaseEntity<UUID> {


    private String cin;
    private String email;
    private String password;
}
