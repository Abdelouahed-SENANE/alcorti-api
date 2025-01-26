package ma.youcode.api.entities.users;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.youcode.api.constants.RoleName;
import org.springframework.data.domain.Auditable;
import org.starter.utilities.entities.BaseEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;



@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "USERS")
@DiscriminatorColumn(name = "role_name")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity<UUID> {

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
    private Boolean isActive;
    @Column(name = "is_email_verified" , nullable = false)
    private Boolean isEmailVerified;
    @Column(name = "logged_at")
    private LocalDateTime loggedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name" , insertable = false , updatable = false)
    private RoleName role;


//    public User(User user) {
//        super.setId(user.getId());
//        this.firstName = user.getFirstName();
//        this.lastName = user.getLastName();
//        this.email = user.getEmail();
//        this.password = user.getPassword();
//        this.isActive = user.getIsActive();
//        this.isEmailVerified = user.getIsEmailVerified();
//        this.loggedAt = user.getLoggedAt();
//        this.role = user.getRole();
//    }

}
