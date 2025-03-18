package ma.youcode.api.repositories;

import ma.youcode.api.models.Payment;
import org.springframework.stereotype.Repository;
import org.starter.utilities.repositories.GenericRepository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends GenericRepository<Payment, UUID> {

}
