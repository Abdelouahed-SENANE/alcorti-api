package ma.youcode.api.models.tokens;

import jakarta.persistence.*;
import lombok.*;
import ma.youcode.api.models.users.User;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "REFRESH_TOKEN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "token_id")
    private UUID id;

    @Column(name = "token" , unique = true , nullable = false)
    private String token;
    @Column(name = "expiry_date" , nullable = false)
    private Instant expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User user;

}
