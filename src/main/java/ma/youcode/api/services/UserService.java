package ma.youcode.api.services;

import ma.youcode.api.enums.UserType;
import ma.youcode.api.models.users.User;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.UserResponse;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.services.CrudService;

import java.util.UUID;

public interface UserService extends CrudService<UserResponse, UserRequest,User , UUID> {
    void create(UserRequest requestDTO , UserType userType);
    void disableAccount(UUID uuid);
    void enableAccount(UUID uuid);
    void logout(UserSecurity user);
    UserResponse readCurrentUser(UserSecurity user);
    void updatePhoto(UUID uuid , MultipartFile image);
    User findById(UUID uuid);
}
