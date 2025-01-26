package ma.youcode.api.repositories;

import ma.youcode.api.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.starter.utilities.repositories.GenericRepository;

import java.util.UUID;

@Repository
public interface UserRepository extends GenericRepository<User, UUID> {

}
