package ma.youcode.api.services.implementations;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.annotations.CurrentUser;
import ma.youcode.api.constants.UserType;
import ma.youcode.api.models.users.User;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.UserResponse;
import ma.youcode.api.repositories.UserRepository;
import ma.youcode.api.security.services.UserSecurity;
import ma.youcode.api.services.FileStorageService;
import ma.youcode.api.services.UserService;
import ma.youcode.api.utilities.factories.UserFactory;
import ma.youcode.api.utilities.mappers.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starter.utilities.mappers.GenericMapper;
import org.starter.utilities.repositories.GenericRepository;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Override
    public GenericRepository<User, UUID> getRepository() {
        return userRepository;
    }

    @Override
    public GenericMapper<User, UserResponse, UserRequest> getMapper() {
        return userMapper;
    }

    @Override
    public void create(UserRequest dto , UserType userType) {
        User user = UserFactory.build(dto , userType);
        user.setIsAccountNonLocked(false);
        user.setIsEmailVerified(false);
        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);
    }

    @Override
    public UserResponse update(UUID uuid, UserRequest dto) {

        return findAndExecute(uuid , user -> {
            userMapper.updateEntity(dto , user);
            if (dto.picture() != null){
                user.setPicture(fileStorageService.store(dto.picture()));
            }
            return userMapper.toResponseDTO(user);
        });

    }

    @Override
    public void lockAccount(UUID uuid) {
        findAndExecute(uuid , user -> {
            if (!user.getIsAccountNonLocked()){
                throw new IllegalArgumentException("Account is already locked");
            }
            user.setIsAccountNonLocked(false);
            userRepository.save(user);
        });
    }

    @Override
    public void unLockAccount(UUID uuid) {
        findAndExecute(uuid , user -> {
            if (user.getIsAccountNonLocked()){
                throw new IllegalArgumentException("Account is already unlocked");
            }
            user.setIsAccountNonLocked(true);
            userRepository.save(user);
        });
    }

    @Override
    public void logout(UserSecurity user) {
        refreshTokenService.loadRefreshTokenByUserId(user.getId()).ifPresent(refreshTokenService::delete);
    }

    @Override
    public UserResponse readCurrentUser(@CurrentUser UserSecurity user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .cin(user.getCin())
                .picture(user.getPicture())
                .role(user.getRole())
                .build();
    }
}
