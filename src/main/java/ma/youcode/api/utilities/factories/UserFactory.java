package ma.youcode.api.utilities.factories;

import ma.youcode.api.constants.UserType;
import ma.youcode.api.dtos.requests.UserRequestDTO;
import ma.youcode.api.entities.users.User;

public interface UserFactory {
    User build(UserRequestDTO dto , UserType userType);
}
