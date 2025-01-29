package ma.youcode.api.services;

import ma.youcode.api.constants.UserType;
import ma.youcode.api.payload.requests.UserRequestDTO;
import ma.youcode.api.payload.responses.UserResponseDTO;
import ma.youcode.api.models.users.User;
import org.starter.utilities.services.CrudService;

import java.util.UUID;

public interface UserService extends CrudService<UserResponseDTO , UserRequestDTO,User , UUID> {
    void create(UserRequestDTO requestDTO , UserType userType);
    void lockAccount(UUID uuid);
    void unLockAccount(UUID uuid);
    void logout();
}
