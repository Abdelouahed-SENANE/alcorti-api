package ma.youcode.api.controllers;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.constants.UserType;
import ma.youcode.api.dtos.requests.LoginRequestDTO;
import ma.youcode.api.dtos.requests.UserRequestDTO;
import ma.youcode.api.dtos.responses.LoginResponseDTO;
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
    public ResponseEntity<SimpleSuccessDTO> handleRegisterCustomer(@RequestBody @Validated({OnCreate.class}) UserRequestDTO request) {
        authService.register(request, UserType.CUSTOMER);
        return simpleSuccess(201, "Customer created successfully.");
    }

    @PostMapping(value = {"/register/drivers"})
    public ResponseEntity<SimpleSuccessDTO> handleRegisterDriver(@RequestBody @Validated({OnCreate.class}) UserRequestDTO request) {
        authService.register(request, UserType.DRIVER);
        return simpleSuccess(201, "Driver created successfully.");
    }

    @PostMapping(value = {"/login"})
    public ResponseEntity<SimpleSuccessDTO> handleLogin(@RequestBody @Validated({OnCreate.class}) LoginRequestDTO request) {
        LoginResponseDTO responseDTO = authService.login(request);
        return simpleSuccess(200, "Logged in successfully." , responseDTO);
    }


}
