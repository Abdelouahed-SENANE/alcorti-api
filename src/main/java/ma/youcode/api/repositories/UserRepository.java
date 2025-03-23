package ma.youcode.api.repositories;

import ma.youcode.api.enums.RoleType;
import ma.youcode.api.models.users.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.starter.utilities.repositories.GenericRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends GenericRepository<User, UUID>  , JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u WHERE u.cin = :value OR u.email = :value")
    Optional<User> findByEmailOrCin(@Param("value") String value);
    long countByRole(RoleType role);
    long countByCreatedAtBetween(LocalDateTime before, LocalDateTime after);
    long countByRoleAndCreatedAtBetween(RoleType role, LocalDateTime before, LocalDateTime after);
    @Query("SELECT COUNT (DISTINCT u.loggedAt) FROM User u WHERE u.loggedAt >= :lastWeek")
    long countActiveUsersSince(@Param("lastWeek") LocalDateTime lastWeek);
}
