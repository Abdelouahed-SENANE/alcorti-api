package ma.youcode.api.services;

import ma.youcode.api.annotations.CurrentUser;
import ma.youcode.api.constants.UserType;
import ma.youcode.api.models.users.User;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.UserResponse;
import ma.youcode.api.security.services.UserSecurity;
import org.starter.utilities.services.CrudService;

import java.util.UUID;

public interface UserService extends CrudService<UserResponse, UserRequest,User , UUID> {
    void create(UserRequest requestDTO , UserType userType);
    void lockAccount(UUID uuid);
    void unLockAccount(UUID uuid);
    void logout(UserSecurity user);
    UserResponse readCurrentUser(UserSecurity user);
}
