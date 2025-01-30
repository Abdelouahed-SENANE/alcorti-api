package ma.youcode.api.services.implementations;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.constants.UserType;
import ma.youcode.api.exceptions.auth.RefreshTokenException;
import ma.youcode.api.exceptions.auth.UserLoginException;
import ma.youcode.api.models.tokens.RefreshToken;
import ma.youcode.api.payloads.requests.AuthRequest;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.JwtResponse;
import ma.youcode.api.security.jwt.JwtTokenProvider;
import ma.youcode.api.security.services.UserSecurity;
import ma.youcode.api.services.AuthService;
import ma.youcode.api.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LogManager.getLogger(AuthServiceImpl.class);
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public JwtResponse login(AuthRequest authRequest , HttpServletResponse response) {

        Authentication authentication = getAuthentication(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserSecurity userSecurity = (UserSecurity) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.generateToken(userSecurity);
        String refreshToken = refreshTokenService.createRefreshToken(userSecurity);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expirationTime(jwtTokenProvider.getExpiration())
                .build();

    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtTokenProvider.generateTokenFromUserId(user.getId());
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .expirationTime(jwtTokenProvider.getExpiration())
                            .build();
                }).orElseThrow(() -> {
                    log.error("Refresh token not found");
                    return new RefreshTokenException(refreshToken ,"Missing refresh token in database.Please login again");
                });
    }

    private Authentication getAuthentication(AuthRequest loginDTO) {
        return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.cin(), loginDTO.password())))
                .orElseThrow(() -> new UserLoginException("Could not log in"));
    }

    @Override
    public void register(UserRequest requestDTO, UserType userType) {
        userService.create(requestDTO, userType);
    }

}
