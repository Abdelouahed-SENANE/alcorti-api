package ma.youcode.api.models.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ma.youcode.api.models.Post;

import java.util.HashSet;
import java.util.Set;


@SuperBuilder(toBuilder = true)
@Getter
@Setter
@DiscriminatorValue("ROLE_CUSTOMER")
@Entity
@Table(name = "CUSTOMERS")
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends User {

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "customer" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();


}
