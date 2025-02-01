package ma.youcode.api.security.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.exceptions.auth.InvalidTokenRequestException;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.security.services.UserDetailsServiceImpl;
import ma.youcode.api.utilities.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LogManager.getLogger(JwtAuthenticationFilter.class);

    private @Value("${JWT_HEADER}") String AUTHORIZATION;
    private @Value("${JWT_PREFIX}") String BEARER;

    private final JwtTokenValidator jwtTokenValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = extractTokenFromRequest(request);
            if (StringUtils.hasText(token)) {
                authenticateToken(token, request);
            }
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            log.error("Failed to set user authentication: ", ex);
            throw new InvalidTokenRequestException("JWT", ex.getMessage(), "Token validation failed");
        }
    }

    /**
     * Extract the token from the request
      * @param request the http request
     * @return token from the request
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.replace(BEARER + " ", "");
        }
        log.info("Bearer token not found in request header");
        return null;
    }

    /**
     * Authenticate the token in the request header
     * @param token the token in the request header
     * @param request type of HttpServletRequest
     */
    private void authenticateToken(String token , HttpServletRequest  request) {
        String fingerprint = extractFingerprintFromCookie(request);
        String hashedFingerprint = Utils.hashFingerprint(fingerprint);

        if (jwtTokenValidator.validateToken(token) && jwtTokenValidator.validateFingerprint(token, hashedFingerprint)) {
            String cinOrEmail = jwtTokenProvider.getCinOrEmailFromToken(token);
            UserSecurity userSecurity = (UserSecurity) userDetailsService.loadUserByUsername(cinOrEmail);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userSecurity, token, userSecurity.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }


    }

    /**
     * this function to extract the fingerprint from the cookie
     * @param request the http request
     * @return fingerprint from the cookie
     */
    private String extractFingerprintFromCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("__Secure-Fgp"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
