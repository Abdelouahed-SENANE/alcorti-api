package ma.youcode.api.services.implementations;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.constants.UserType;
import ma.youcode.api.dtos.requests.UserRequestDTO;
import ma.youcode.api.dtos.responses.UserResponseDTO;
import ma.youcode.api.entities.users.Admin;
import ma.youcode.api.entities.users.Customer;
import ma.youcode.api.entities.users.Driver;
import ma.youcode.api.entities.users.User;
import ma.youcode.api.repositories.UserRepository;
import ma.youcode.api.services.UserService;
import ma.youcode.api.utilities.factories.UserFactory;
import ma.youcode.api.utilities.mappers.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starter.utilities.mappers.GenericMapper;
import org.starter.utilities.repositories.GenericRepository;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserFactory userFactory;


    @Override
    public GenericRepository<User, UUID> getRepository() {
        return userRepository;
    }

    @Override
    public GenericMapper<User, UserResponseDTO, UserRequestDTO> getMapper() {
        return userMapper;
    }

    @Override
    public UserResponseDTO create(UserRequestDTO requestDTO , UserType userType) {
        User user = userFactory.build(requestDTO , userType);
        user.setIsActive(false);
        user.setIsEmailVerified(false);
        return userMapper.toResponseDTO(userRepository.save(user));
    }


}
