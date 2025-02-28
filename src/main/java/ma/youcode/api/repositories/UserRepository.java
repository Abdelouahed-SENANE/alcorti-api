package ma.youcode.api.repositories;

import ma.youcode.api.models.users.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.starter.utilities.repositories.GenericRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends GenericRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.cin = :value OR u.email = :value")
    Optional<User> findByEmailOrCin(@Param("value") String value);
}
