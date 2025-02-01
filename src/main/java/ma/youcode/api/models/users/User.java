package ma.youcode.api.models.users;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.youcode.api.constants.RoleType;
import org.starter.utilities.entities.Auditable;

import java.time.LocalDateTime;
import java.util.UUID;



@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "USERS")
@DiscriminatorColumn(name = "role_name")
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class User extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "cin", unique = true)
    private String cin;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "picture")
    private String picture;
    @Column(name = "is_active" , nullable = false)
    private Boolean active;
    @Column(name = "is_email_verified" , nullable = false)
    private Boolean isEmailVerified;
    @Column(name = "logged_at")
    private LocalDateTime loggedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name" , insertable = false , updatable = false)
    private RoleType role;

    public User(User user) {
        this.id = user.getId();
        this.cin = user.getCin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.picture = user.getPicture();
        this.active = user.getActive();
        this.isEmailVerified = user.getIsEmailVerified();
        this.loggedAt = user.getLoggedAt();
        this.role = user.getRole();
    }
}
