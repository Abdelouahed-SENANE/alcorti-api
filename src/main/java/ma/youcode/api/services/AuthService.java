package ma.youcode.api.services;

import ma.youcode.api.constants.UserType;
import ma.youcode.api.payload.requests.LoginRequestDTO;
import ma.youcode.api.payload.requests.UserRequestDTO;
import ma.youcode.api.payload.responses.LoginResponseDTO;
import ma.youcode.api.payload.responses.UserResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    void register(UserRequestDTO requestDTO , UserType userType);
    void logout();
    LoginResponseDTO refreshToken(String refreshToken);
}
