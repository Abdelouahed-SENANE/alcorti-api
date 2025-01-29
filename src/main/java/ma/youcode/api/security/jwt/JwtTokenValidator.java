package ma.youcode.api.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ma.youcode.api.exceptions.auth.InvalidTokenRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtTokenValidator {

    private static final Logger log = LogManager.getLogger(JwtTokenValidator.class);
    private final String SECRET_KEY;

    public JwtTokenValidator(@Value("${app.jwt.secret.key}") String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(jwt);
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
        }
        return true;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    // TODO : implement check token is already logout
}
