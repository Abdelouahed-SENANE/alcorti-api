package ma.youcode.api.repositories;

import ma.youcode.api.models.tokens.RefreshToken;
import ma.youcode.api.models.users.User;
import org.starter.utilities.repositories.GenericRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends GenericRepository<RefreshToken , Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserId(UUID userId);

    UUID user(User user);
}
