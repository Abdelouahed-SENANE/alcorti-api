package ma.youcode.api.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.security.services.UserDetailsServiceImpl;
import ma.youcode.api.security.services.UserSecurity;
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

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private @Value("${JWT_HEADER}") String AUTHORIZATION;
    private @Value("${JWT_PREFIX}") String BEARER;
    private static final Logger log = LogManager.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenValidator jwtTokenValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractTokenFromRequest(request);

        try {
            if (StringUtils.hasText(token)  && jwtTokenValidator.validateToken(token)) {
                String cin = jwtTokenProvider.getCinFromToken(token);
                UserSecurity userSecurity = (UserSecurity) userDetailsService.loadUserByCin(cin);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userSecurity, token, userSecurity.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Failed to set user authentication in security context: ", ex);
            SecurityContextHolder.clearContext();
            throw ex;
        }
        filterChain.doFilter(request, response);
    }


    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);

        if (bearerToken == null || !bearerToken.startsWith(BEARER)) {
            log.info("Bearer token not found in request header");
            return null;
        }

        return bearerToken.replace(BEARER + " ", "");
    }
//
//    private String extractAccessTokenFromRequest(HttpServletRequest request) {
//        if (request.getCookies() == null) return null;
//        return Arrays.stream(request.getCookies())
//                .filter(cookie -> cookie.getName().equals("access_token"))
//                .findFirst()
//                .map(Cookie::getValue)
//                .orElse(null);
//    }
}
