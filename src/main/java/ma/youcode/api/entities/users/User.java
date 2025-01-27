package ma.youcode.api.entities.users;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.youcode.api.constants.RoleType;
import org.starter.utilities.entities.BaseEntity;

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
    @Column(name = "is_enabled" , nullable = false)
    private Boolean isEnabled;
    @Column(name = "is_email_verified" , nullable = false)
    private Boolean isEmailVerified;
    @Column(name = "logged_at")
    private LocalDateTime loggedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name" , insertable = false , updatable = false)
    private RoleType role;



}
