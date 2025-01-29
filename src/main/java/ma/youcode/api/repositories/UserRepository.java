package ma.youcode.api.repositories;

import ma.youcode.api.models.users.User;
import org.springframework.stereotype.Repository;
import org.starter.utilities.repositories.GenericRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends GenericRepository<User, UUID> {
    Optional<User> findByCin(String cin);
    Optional<User> findByEmail(String email);
}
