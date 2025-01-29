package ma.youcode.api.controllers;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.payload.requests.UserRequestDTO;
import ma.youcode.api.payload.responses.UserResponseDTO;
import ma.youcode.api.models.users.User;
import ma.youcode.api.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.starter.utilities.controllers.DeleteController;
import org.starter.utilities.controllers.ReadAllController;
import org.starter.utilities.controllers.ReadController;
import org.starter.utilities.dtos.SimpleSuccessDTO;
import org.starter.utilities.markers.validation.OnUpdate;
import org.starter.utilities.response.Response;
import org.starter.utilities.services.CrudService;

import java.util.UUID;

import static org.starter.utilities.response.Response.simpleSuccess;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController implements
        ReadAllController<UserResponseDTO, UserRequestDTO, User, UUID>,
        DeleteController<UserResponseDTO, UserRequestDTO, User, UUID>,
        ReadController<UserResponseDTO, UserRequestDTO, User, UUID> {

    private final UserService userService;


    @PutMapping(value = {"/update/{id}/profile"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SimpleSuccessDTO> handleUpdateUser(@PathVariable UUID id,
                                                             @ModelAttribute @Validated({OnUpdate.class}) UserRequestDTO request
    ) {
        UserResponseDTO responseDTO = userService.update(id, request);
        return Response.simpleSuccess(200, "Customer updated successfully.", responseDTO);
    }

    @PatchMapping(value = {"/{id}/lock"})
    public ResponseEntity<SimpleSuccessDTO> handleLockAccount(@PathVariable UUID id
    ) {
        userService.lockAccount(id);
        return Response.simpleSuccess(200, "Account locked successfully.");
    }

    @PatchMapping(value = {"/{id}/unlock"})
    public ResponseEntity<SimpleSuccessDTO> handleUnlockAccount(@PathVariable UUID id
    ) {
         userService.unLockAccount(id);
        return Response.simpleSuccess(200, "Account unlocked successfully.");
    }

    @PostMapping(value = {"/logout"})
    public ResponseEntity<SimpleSuccessDTO> handleLogout() {
        userService.logout();
        return simpleSuccess(200, "Log out successfully.");
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
