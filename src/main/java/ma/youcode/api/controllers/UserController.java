package ma.youcode.api.controllers;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.annotations.AuthUser;
import ma.youcode.api.events.OnUserLogoutSuccessEvent;
import ma.youcode.api.models.users.User;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.UserResponse;
import ma.youcode.api.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
        ReadAllController<UserResponse, UserRequest, User, UUID>,
        DeleteController<UserResponse, UserRequest, User, UUID>,
        ReadController<UserResponse, UserRequest, User, UUID> {

    private static final Logger log = LogManager.getLogger(UserController.class);
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;


    @GetMapping("/me")
    public ResponseEntity<SimpleSuccessDTO> userProfile(@AuthUser UserSecurity user) {
        return Response.simpleSuccess(200, "User profile", userService.readCurrentUser(user));
    }


    @PutMapping(value = {"/profile"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SimpleSuccessDTO> handleUpdateUser(
            @ModelAttribute @Validated({OnUpdate.class}) UserRequest request
            ,@AuthUser UserSecurity userSecurity
    ) {
        userService.updatePhoto(userSecurity.getId(), request.photo());
        UserResponse responseDTO = userService.update(userSecurity.getId(), request);
        return Response.simpleSuccess(200, "User updated successfully.", responseDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = {"/{id}/disable"})
    public ResponseEntity<SimpleSuccessDTO> disableAccount(@PathVariable UUID id
    ) {
        userService.disableAccount(id);
        return Response.simpleSuccess(200, "Account locked successfully.");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = {"/{id}/enable"})
    public ResponseEntity<SimpleSuccessDTO> enableAccount(@PathVariable UUID id
    ) {
         userService.enableAccount(id);
        return Response.simpleSuccess(200, "Account unlocked successfully.");
    }


    @PostMapping(value = {"/logout"})
    public ResponseEntity<SimpleSuccessDTO> logout(@ma.youcode.api.annotations.AuthUser UserSecurity user) {
        userService.logout(user);
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(user.getCin() , credentials.toString());
        eventPublisher.publishEvent(logoutSuccessEvent);
        return simpleSuccess(200, "Log out successfully.");
    }

    @Override
    public CrudService<UserResponse, UserRequest, User, UUID> getService() {
        return userService;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}
