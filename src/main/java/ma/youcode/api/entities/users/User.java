package ma.youcode.api.entities.users;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.youcode.api.constants.RoleName;
import org.starter.utilities.entities.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;



@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "USERS")
@DiscriminatorColumn(name = "ROLE")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User extends BaseEntity<UUID> {

    @Column(name = "CIN", unique = true)
    private String cin;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "EMAIL", unique = true)
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "IS_ACTIVE" , nullable = false)
    private Boolean isActive;
    @Column(name = "IS_EMAIL_VERIFIED" , nullable = false)
    private Boolean isEmailVerified;
    @Column(name = "LOGGED_AT")
    private LocalDateTime loggedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_NAME" , insertable = false , updatable = false)
    private RoleName role;


    public User(User user) {
        super.setId(user.getId());
        this.cin = user.getCin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.isActive = user.getIsActive();
        this.isEmailVerified = user.getIsEmailVerified();
        this.loggedAt = user.getLoggedAt();
        this.role = user.getRole();
    }

}
