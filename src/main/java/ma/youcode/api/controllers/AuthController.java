package ma.youcode.api.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.enums.UserType;
import ma.youcode.api.payloads.requests.AuthRequest;
import ma.youcode.api.payloads.requests.RefreshTokenRequest;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.AuthResponse;
import ma.youcode.api.payloads.responses.UserResponse;
import ma.youcode.api.services.AuthService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.starter.utilities.dtos.SimpleSuccessDTO;
import org.starter.utilities.markers.validation.OnCreate;
import org.starter.utilities.response.Response;

import java.util.Optional;

import static org.starter.utilities.response.Response.simpleSuccess;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/me")
    public ResponseEntity<?> userProfile() {
        Optional<UserResponse> authUserOptional = Optional.ofNullable(authService.loadMe());
        return authUserOptional
                .map(authUser -> Response.simpleSuccess(200, "User profile", authUser))
                .orElseGet(() -> Response.simpleSuccess(200, "User not authenticated.", Optional.empty()));
    }

    @PostMapping(value = {"/register"})
    public ResponseEntity<SimpleSuccessDTO> registerDriver(@RequestBody @Validated({OnCreate.class}) UserRequest request) {
        authService.register(request);
        return simpleSuccess(201, request.userType().toString().toLowerCase() + " created successfully.");
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailInUse(@RequestParam(name = "email") String email) {
        return ResponseEntity.ok().body(authService.emailAlreadyExists(email));
    }

    @GetMapping("/check-cin")
    public ResponseEntity<?> checkCinInUse(@RequestParam(name = "cin") String cin) {
        return ResponseEntity.ok().body(authService.cinAlreadyExists(cin));
    }

    @PostMapping(value = {"/login"})
    public ResponseEntity<SimpleSuccessDTO> login(@RequestBody @Valid AuthRequest request, HttpServletResponse response) {

        AuthResponse responseDTO = authService.login(request, response);
        return simpleSuccess(200, "Logged in successfully.", responseDTO);
    }

    @PostMapping(value = {"/refresh"})
    public ResponseEntity<SimpleSuccessDTO> refreshToken(@RequestBody @Valid RefreshTokenRequest dto , HttpServletResponse response) {
        AuthResponse responseDTO = authService.refresh(dto.refreshToken() , response);
        return simpleSuccess(200, "Token is refreshed successfully.", responseDTO);
    }


}
