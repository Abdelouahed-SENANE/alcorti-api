package ma.youcode.api.entities.users;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@SuperBuilder(toBuilder = true)
@Getter
@Setter
@DiscriminatorValue("ROLE_COSTUMER")
@Entity
@Table(name = "CUSTOMERS")
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends User{

    @Column(name = "phone_number")
    private String phoneNumber;

}
