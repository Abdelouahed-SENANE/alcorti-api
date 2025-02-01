package ma.youcode.api.services;

import jakarta.servlet.http.HttpServletResponse;
import ma.youcode.api.constants.UserType;
import ma.youcode.api.payloads.requests.AuthRequest;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.JwtResponse;

public interface AuthService {

    JwtResponse login(AuthRequest authRequest , HttpServletResponse response);
    void register(UserRequest requestDTO , UserType userType);
    JwtResponse refresh( String refreshToken);
}
