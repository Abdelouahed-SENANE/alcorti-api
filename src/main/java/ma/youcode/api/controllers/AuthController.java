package ma.youcode.api.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.enums.UserType;
import ma.youcode.api.payloads.requests.AuthRequest;
import ma.youcode.api.payloads.requests.RefreshTokenRequest;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.JwtResponse;
import ma.youcode.api.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starter.utilities.dtos.SimpleSuccessDTO;
import org.starter.utilities.markers.validation.OnCreate;

import static org.starter.utilities.response.Response.simpleSuccess;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = {"/register/customers"})
    public ResponseEntity<SimpleSuccessDTO> handleRegisterCustomer(@RequestBody @Validated({OnCreate.class}) UserRequest request) {
        authService.register(request, UserType.CUSTOMER);
        return simpleSuccess(201, "Customer created successfully.");
    }

    @PostMapping(value = {"/register/drivers"})
    public ResponseEntity<SimpleSuccessDTO> handleRegisterDriver(@RequestBody @Validated({OnCreate.class}) UserRequest request) {
        authService.register(request, UserType.DRIVER);
        return simpleSuccess(201, "Driver created successfully.");
    }

    @PostMapping(value = {"/login"})
    public ResponseEntity<SimpleSuccessDTO> handleLogin(@RequestBody @Valid AuthRequest request , HttpServletResponse response) {

        JwtResponse responseDTO = authService.login(request , response);
        return simpleSuccess(200, "Logged in successfully." , responseDTO);
    }

    @PostMapping(value = {"/refresh"})
    public ResponseEntity<SimpleSuccessDTO> handleRefreshToken(@RequestBody @Valid RefreshTokenRequest dto) {
        JwtResponse responseDTO = authService.refresh(dto.refreshToken());
        return simpleSuccess(200, "Token is refreshed successfully." , responseDTO);
    }


}
