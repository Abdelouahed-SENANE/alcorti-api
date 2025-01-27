package ma.youcode.api.controllers;

import jakarta.validation.Valid;
import ma.youcode.api.constants.UserType;
import ma.youcode.api.dtos.requests.UserRequestDTO;
import ma.youcode.api.dtos.responses.UserResponseDTO;
import ma.youcode.api.entities.users.User;
import ma.youcode.api.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.controllers.DeleteController;
import org.starter.utilities.controllers.ReadAllController;
import org.starter.utilities.controllers.ReadController;
import org.starter.utilities.controllers.UpdateController;
import org.starter.utilities.dtos.SimpleSuccessDTO;
import org.starter.utilities.markers.validation.OnCreate;
import org.starter.utilities.markers.validation.OnUpdate;
import org.starter.utilities.response.Response;
import org.starter.utilities.services.CrudService;

import java.util.UUID;

@RestController
@RequestMapping("api/users")
public class UserController implements
        ReadAllController<UserResponseDTO, UserRequestDTO, User, UUID>,
        DeleteController<UserResponseDTO, UserRequestDTO, User, UUID>,
        ReadController<UserResponseDTO, UserRequestDTO, User, UUID> {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = {"/new/customers"})
    public ResponseEntity<SimpleSuccessDTO> handleCreateCustomer(@RequestBody @Validated({OnCreate.class}) UserRequestDTO request) {
        UserResponseDTO responseDTO = userService.create(request, UserType.CUSTOMER);
        return Response.simpleSuccess(201, "Customer created successfully.", responseDTO);
    }

    @PostMapping(value = {"/new/drivers"})
    public ResponseEntity<SimpleSuccessDTO> handleCreateDriver(@RequestBody @Validated({OnCreate.class}) UserRequestDTO request) {
        UserResponseDTO responseDTO = userService.create(request, UserType.DRIVER);
        return Response.simpleSuccess(201, "Driver created successfully.", responseDTO);
    }

    @PutMapping(value = {"/update/{id}/profile"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SimpleSuccessDTO> handleUpdateUser(@PathVariable UUID id,
                                                             @ModelAttribute @Validated({OnUpdate.class}) UserRequestDTO request
    ) {
        UserResponseDTO responseDTO = userService.update(id, request);
        return Response.simpleSuccess(200, "Customer updated successfully.", responseDTO);
    }


    @Override
    public CrudService<UserResponseDTO, UserRequestDTO, User, UUID> getService() {
        return userService;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}
