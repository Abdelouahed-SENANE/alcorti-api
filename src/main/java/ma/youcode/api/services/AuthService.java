package ma.youcode.api.services;

import ma.youcode.api.constants.UserType;
import ma.youcode.api.dtos.requests.LoginRequestDTO;
import ma.youcode.api.dtos.requests.UserRequestDTO;
import ma.youcode.api.dtos.responses.LoginResponseDTO;
import ma.youcode.api.dtos.responses.UserResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    void register(UserRequestDTO requestDTO , UserType userType);
    void logout();
    UserResponseDTO getAuthenticatedUser();
}
