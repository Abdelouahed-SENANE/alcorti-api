package ma.youcode.api.security.services;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.exceptions.ResourceNotFoundException;
import ma.youcode.api.exceptions.auth.RefreshTokenException;
import ma.youcode.api.models.tokens.RefreshToken;
import ma.youcode.api.models.users.User;
import ma.youcode.api.repositories.RefreshTokenRepository;
import ma.youcode.api.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final Logger log = LogManager.getLogger(RefreshTokenService.class);
    @Value("${app.jwt.refresh.expiration}")
    private Long refreshTokenExpirationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken createRefreshToken(UserPrincipal user) {

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId())
                .orElse(new RefreshToken());
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString()); // Set other properties as needed
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationMs)); // Example expiry date


//         refreshTokenRepository.findByUserId(user.getId())
//                .map(RefreshToken::getId)
//                .ifPresent(refreshTokenRepository::deleteById);
//
//        RefreshToken refreshToken = RefreshToken.builder()
//                .user(user)
//                .expiryDate(Instant.now().plusMillis(refreshTokenExpirationMs))
//                .token(UUID.randomUUID().toString())
//                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException(refreshToken.getToken(), ("Refresh token is expired."));
        }
        return refreshToken;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


}
