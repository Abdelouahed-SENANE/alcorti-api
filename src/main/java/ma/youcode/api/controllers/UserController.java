package ma.youcode.api.controllers;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.annotations.AuthUser;
import ma.youcode.api.events.OnUserLogoutSuccessEvent;
import ma.youcode.api.models.users.User;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.payloads.requests.DriverCompleteRequest;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.UserResponse;
import ma.youcode.api.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

import java.util.Optional;
import java.util.UUID;

import static org.starter.utilities.response.Response.simpleSuccess;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LogManager.getLogger(UserController.class);
    private static  final String DEFAULT_PAGE = "0";
    private static  final String DEFAULT_SIZE = "10";
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @PutMapping("/drivers/complete-profile")
    public ResponseEntity<SimpleSuccessDTO> driverCompleteRegistering(@ModelAttribute DriverCompleteRequest request) {
        userService.finalizeDriverRegistration(request);
        return simpleSuccess(HttpStatus.OK.value(), "Profile completed successfully." );
    }

    @PutMapping(value = {"users/profile"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SimpleSuccessDTO> handleUpdateUser(
            @ModelAttribute @Validated({OnUpdate.class}) UserRequest request
            ,@AuthUser UserSecurity userSecurity
    ) {
        if (request.photo() != null) {
            userService.updatePhoto(userSecurity.getId(), request.photo());
        }
        UserResponse responseDTO = userService.update(userSecurity.getId(), request);
        return Response.simpleSuccess(200, "User updated successfully.", responseDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = {"users/{id}/account-status"})
    public ResponseEntity<SimpleSuccessDTO> disableAccount(@PathVariable UUID id , @RequestParam Boolean active
    ) {
        userService.modifyAccountStatus(id , active);
        String message = active ? "Account enabled successfully." : "Account disabled successfully.";
        return Response.simpleSuccess(200, message);
    }

    @PostMapping(value = {"users/logout"})
    public ResponseEntity<SimpleSuccessDTO> logout(@AuthUser UserSecurity user) {
        userService.logout(user);
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(user.getCin() , credentials.toString());
        eventPublisher.publishEvent(logoutSuccessEvent);
        return simpleSuccess(200, "Log out successfully.");
    }

    @GetMapping("users/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SimpleSuccessDTO> readAllUsers(@RequestParam(value = "search", required = false) String search,
                                                         @RequestParam(value = "page", defaultValue = DEFAULT_PAGE) int page, @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        return simpleSuccess(HttpStatus.OK.value(), "Users fetched successfully.", userService.loadAllUsers(pageable , search));
    }

    @DeleteMapping("users/{userId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<SimpleSuccessDTO> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return simpleSuccess(HttpStatus.NO_CONTENT.value(), "Shipment deleted successfully.");
    }
}
