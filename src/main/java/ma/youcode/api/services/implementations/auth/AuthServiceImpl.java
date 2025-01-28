package ma.youcode.api.services.implementations.auth;


import lombok.RequiredArgsConstructor;
import ma.youcode.api.constants.UserType;
import ma.youcode.api.dtos.requests.LoginRequestDTO;
import ma.youcode.api.dtos.requests.UserRequestDTO;
import ma.youcode.api.dtos.responses.LoginResponseDTO;
import ma.youcode.api.dtos.responses.UserResponseDTO;
import ma.youcode.api.services.AuthService;
import ma.youcode.api.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        return null;
    }

    @Override
    public void register(UserRequestDTO requestDTO , UserType userType) {
        userService.create(requestDTO , userType);
    }

    @Override
    public UserResponseDTO getAuthenticatedUser() {
        return null;
    }

    @Override
    public void logout() {

    }
}
