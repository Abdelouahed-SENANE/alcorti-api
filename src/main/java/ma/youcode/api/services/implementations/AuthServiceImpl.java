package ma.youcode.api.services.implementations;


import lombok.RequiredArgsConstructor;
import ma.youcode.api.constants.UserType;
import ma.youcode.api.exceptions.ResourceNotFoundException;
import ma.youcode.api.exceptions.auth.RefreshTokenException;
import ma.youcode.api.models.tokens.RefreshToken;
import ma.youcode.api.payload.requests.LoginRequestDTO;
import ma.youcode.api.payload.requests.UserRequestDTO;
import ma.youcode.api.payload.responses.LoginResponseDTO;
import ma.youcode.api.payload.responses.UserResponseDTO;
import ma.youcode.api.exceptions.auth.UserLoginException;
import ma.youcode.api.security.jwt.JwtTokenProvider;
import ma.youcode.api.security.services.RefreshTokenService;
import ma.youcode.api.security.services.UserPrincipal;
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
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = getAuthentication(loginRequestDTO);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.generateToken(userPrincipal);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal);

        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expirationTime(jwtTokenProvider.getExpiration())
                .build();

    }

    @Override
    public LoginResponseDTO refreshToken(String refreshToken) {

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtTokenProvider.generateTokenFromUserId(user.getId());
                    return LoginResponseDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .expirationTime(jwtTokenProvider.getExpiration())
                            .build();
                }).orElseThrow(() -> {
                    log.error("Refresh token not found");
                    return new RefreshTokenException(refreshToken ,"Missing refresh token in database.Please login again");
                });
    }

    private Authentication getAuthentication(LoginRequestDTO loginDTO) {
        return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.cin(), loginDTO.password())))
                .orElseThrow(() -> new UserLoginException("Could not log in"));
    }

    @Override
    public void register(UserRequestDTO requestDTO, UserType userType) {
        userService.create(requestDTO, userType);
    }


    @Override
    public void logout() {

    }
}
