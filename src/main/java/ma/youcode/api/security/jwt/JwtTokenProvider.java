package ma.youcode.api.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ma.youcode.api.models.users.UserSecurity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private static final Logger log = LogManager.getLogger(JwtTokenProvider.class);
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.expiration}")
    private long JWT_EXPIRATION;


    public String generateToken(String cin, String hashFingerprint) {
        Instant expiryDate = Instant.now().plusMillis(JWT_EXPIRATION);

        return Jwts.builder()
                .subject(cin)
                .claim("userFingerprint" , hashFingerprint)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiryDate))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }
    public Claims decodeToken(String token) {
        return  Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String getCinOrEmailFromToken(String token) {
        Claims claims = decodeToken(token);
        String cin = claims.getSubject();
        String email = claims.get("email", String.class);
        return cin != null ? cin : email;

    }

    public Date getExpirationFromToken(String token) {
        return  Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

//    public String generateTokenFromUserId(UUID userId , String hashFingerprint) {
//        Instant expiryDate = Instant.now().plusMillis(JWT_EXPIRATION);
//        Map<String , String> claims = Map.of("userFingerprint", hashFingerprint);
//
//        return Jwts.builder()
//                .subject(userId.toString())
//                .claims(claims)
//                .issuedAt(Date.from(Instant.now()))
//                .expiration(Date.from(expiryDate))
//                .signWith(getSigningKey(), Jwts.SIG.HS256)
//                .compact();
//    }

    public String getFingerprintFromToken(String token) {
        return  Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userFingerprint", String.class);
    }

    public long getExpiration() {
        return JWT_EXPIRATION;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
