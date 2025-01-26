package ma.youcode.api.services;

import ma.youcode.api.constants.UserType;
import ma.youcode.api.dtos.requests.UserRequestDTO;
import ma.youcode.api.dtos.responses.UserResponseDTO;
import ma.youcode.api.entities.users.User;
import org.starter.utilities.services.CrudService;

import java.util.UUID;

public interface UserService extends CrudService<UserResponseDTO , UserRequestDTO,User , UUID> {
    public UserResponseDTO create(UserRequestDTO requestDTO , UserType userType);
}
