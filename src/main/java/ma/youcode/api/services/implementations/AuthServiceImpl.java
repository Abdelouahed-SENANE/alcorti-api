package ma.youcode.api.services.implementations;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.enums.UserType;
import ma.youcode.api.exceptions.auth.RefreshTokenException;
import ma.youcode.api.models.tokens.RefreshToken;
import ma.youcode.api.models.users.User;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.payloads.requests.AuthRequest;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.AuthResponse;
import ma.youcode.api.payloads.responses.JwtResponse;
import ma.youcode.api.payloads.responses.UserResponse;
import ma.youcode.api.security.jwt.JwtTokenProvider;
import ma.youcode.api.services.AuthService;
import ma.youcode.api.services.UserService;
import ma.youcode.api.utilities.Utils;
import ma.youcode.api.utilities.factories.UserResponseFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

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
    public AuthResponse login(AuthRequest authRequest, HttpServletResponse response) {

        Authentication authentication = authenticateUser(authRequest);
        UserSecurity userSecurity = (UserSecurity) authentication.getPrincipal();
        String fingerprint = Utils.generateFingerprint();
        String hashedFingerprint = Utils.hashFingerprint(fingerprint);
        String accessToken = jwtTokenProvider.generateToken(userSecurity.getCin(), hashedFingerprint);
        String refreshToken = refreshTokenService.createRefreshToken(userSecurity);

        setFingerprintCookie(response, fingerprint);

        return AuthResponse.builder()
                .jwt(JwtResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .expiresIn(jwtTokenProvider.getExpiration())
                        .build())
                .user(loadMe())
                .build();

    }


    @Override
    public AuthResponse refresh(String refreshToken , HttpServletResponse response) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String fingerprint = Utils.generateFingerprint();
                    String hashedFingerprint = Utils.hashFingerprint(fingerprint);
                    String accessToken = jwtTokenProvider.generateToken(user.getCin() , hashedFingerprint);
                    setFingerprintCookie(response, fingerprint);
                    return AuthResponse.builder()
                            .jwt(JwtResponse.builder()
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .expiresIn(jwtTokenProvider.getExpiration())
                                    .build())
                            .build();
                }).orElseThrow(() -> {
                    log.error("Refresh token not found");
                    return new RefreshTokenException(refreshToken, "Missing refresh token in database.Please login again");
                });
    }

    private void setFingerprintCookie(HttpServletResponse response, String fingerprint) {
        ResponseCookie fingerprintCookie = Utils.buildFingerprintCookie(fingerprint);
        response.setHeader("Set-Cookie", fingerprintCookie.toString());
    }

    private Authentication authenticateUser(AuthRequest loginDTO) {
        String cinOrEmail = loginDTO.email() != null ? loginDTO.email() : loginDTO.cin();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(cinOrEmail, loginDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    public void register(UserRequest requestDTO) {
        userService.create(requestDTO);
    }

    @Override
    public boolean emailAlreadyExists(String email) {
        return userService.emailExists(email);
    }

    @Override
    public boolean cinAlreadyExists(String cin) {
        return userService.cinExists(cin);
    }

    @Override
    public UserResponse loadMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return Optional.ofNullable(authentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof UserSecurity)
                .map(principal -> (UserSecurity) principal)
                .map(UserSecurity::getId)
                .map(userService::findById)
                .map(UserResponseFactory::build)
                .orElse(null);

    }
}
