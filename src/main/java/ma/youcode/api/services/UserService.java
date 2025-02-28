package ma.youcode.api.services;

import ma.youcode.api.enums.UserType;
import ma.youcode.api.models.users.User;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.payloads.requests.DriverCompleteRequest;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.UserResponse;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.services.CrudService;
import org.starter.utilities.services.crud.DeleteService;
import org.starter.utilities.services.crud.ReadAllService;
import org.starter.utilities.services.crud.ReadByIdService;
import org.starter.utilities.services.crud.UpdateService;

import java.util.UUID;

public interface UserService extends UpdateService<UserResponse, UserRequest,User , UUID>,
        DeleteService<UserResponse, UserRequest,User , UUID>,
        ReadAllService<UserResponse, UserRequest,User , UUID>,
        ReadByIdService<UserResponse, UserRequest,User , UUID> {
    void create(UserRequest requestDTO);
    void disableAccount(UUID uuid);
    void enableAccount(UUID uuid);
    void logout(UserSecurity user);
    void updatePhoto(UUID uuid , MultipartFile image);
    User findById(UUID uuid);
    boolean emailExists(String email);
    boolean cinExists(String email);
    void finalizeDriverRegistration(DriverCompleteRequest request);
}
