package ma.youcode.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.services.implementations.auth.UserDetailsServiceImpl;
import ma.youcode.api.services.implementations.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${app.jwt.header}")
    private String tokenHeader;
    @Value("${app.jwt.prefix}")
    private String tokenPrefix;
    @Autowired
    private  JwtTokenValidator jwtTokenValidator;
    @Autowired
    private  JwtTokenProvider jwtTokenProvider;
    @Autowired
    private  UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractTokenFromRequest(request);
        try {
            if (StringUtils.hasText(token) && jwtTokenValidator.validateToken(token)) {
                String cin = jwtTokenProvider.getCinFromToken(token);
                UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByCin(cin);
                List<GrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromJwt(token);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal, token, authorities);
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
        String bearerToken = request.getHeader(tokenHeader);
        if (bearerToken == null || !bearerToken.startsWith(tokenPrefix)) {
            return null;
        }
        return bearerToken.replace(tokenPrefix, "");
    }
}
