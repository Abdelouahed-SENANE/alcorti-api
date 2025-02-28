package ma.youcode.api.services;

import jakarta.servlet.http.HttpServletResponse;
import ma.youcode.api.enums.UserType;
import ma.youcode.api.payloads.requests.AuthRequest;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.AuthResponse;
import ma.youcode.api.payloads.responses.UserResponse;

import java.util.Optional;

public interface AuthService {

    boolean emailAlreadyExists(String email);
    boolean cinAlreadyExists(String cin);
    AuthResponse login(AuthRequest authRequest , HttpServletResponse response);
    void register(UserRequest requestDTO);
    AuthResponse refresh(String refreshToken , HttpServletResponse response);
    UserResponse loadMe();

}
