package ma.youcode.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ma.youcode.api.services.implementations.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component

public class JwtTokenProvider {

    private final String SECRET_KEY;
    private final long JWT_EXPIRATION;

    public JwtTokenProvider(@Value("${app.jwt.expiration}") long jwtExpiration , @Value("${app.jwt.secret.key}") String secretKey) {
        this.JWT_EXPIRATION = jwtExpiration;
        this.SECRET_KEY = secretKey;
    }

    public String generateToken(UserPrincipal userPrincipal) {
        Instant expiryDate = Instant.now().plusMillis(JWT_EXPIRATION);
        String authorities = getAuthorities(userPrincipal);
        return Jwts.builder()
                .claims()
                .subject(userPrincipal.getCin())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiryDate))
                .and()
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .claim("authorities", authorities)
                .compact();
    }
    public String getCinFromToken(String token) {
        return  Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Date getExpiryFromToken(String token) {
        return  Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

    }

    public long getExpiration(String token) {
        return JWT_EXPIRATION;
    }

    public List<GrantedAuthority> getAuthoritiesFromJwt(String token) {
        Claims claims =Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Arrays.stream(claims.get("authorities").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String getAuthorities(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

}
