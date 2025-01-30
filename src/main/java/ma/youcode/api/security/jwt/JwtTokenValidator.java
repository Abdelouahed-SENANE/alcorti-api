package ma.youcode.api.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.cache.LoggedOutTokenCache;
import ma.youcode.api.events.OnUserLogoutSuccessEvent;
import ma.youcode.api.exceptions.auth.InvalidTokenRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private static final Logger log = LogManager.getLogger(JwtTokenValidator.class);
    @Value("${JWT_SECRET}")
    private  String SECRET_KEY;
    private final LoggedOutTokenCache loggedOutTokenCache;

    public boolean validateToken(String jwt) {

        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(jwt);

        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            throw new InvalidTokenRequestException("JWT", jwt, "Invalid token");

        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            throw new InvalidTokenRequestException("JWT", jwt, "Token expired. Refresh required");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throw new InvalidTokenRequestException("JWT", jwt, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throw new InvalidTokenRequestException("JWT", jwt, "Illegal argument token");
        } catch (JwtException ex) {
            log.error("Invalid JWT signature");
            throw new InvalidTokenRequestException("JWT", jwt, "Incorrect signature");
        }catch (Exception ex) {
            log.error("Error validating fingerprint");
            throw new InvalidTokenRequestException("JWT", jwt, "Fingerprint validation failed");
        }
        validateIsNotForLoggedOutToken(jwt);
        return true;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private void validateIsNotForLoggedOutToken(String token) {
        OnUserLogoutSuccessEvent previousLogoutEvent = loggedOutTokenCache.getLogoutEventFromToken(token);
        log.info("Token is [{}]", loggedOutTokenCache.getLogoutEventFromToken(token));
        log.info("Token is [{}]", token);
        if (previousLogoutEvent != null) {
            Date eventTime = previousLogoutEvent.getEventTime();
            String userCin = previousLogoutEvent.getUserCin();
            log.info("Token corresponds to an already logged out user [{}] at [{}]. Please login again", userCin , eventTime); ;
            throw new InvalidTokenRequestException("JWT", token, "Token is already logged out");
        }
    }

}
