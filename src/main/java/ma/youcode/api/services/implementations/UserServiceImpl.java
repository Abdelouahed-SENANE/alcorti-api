package ma.youcode.api.services.implementations;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.annotations.AuthUser;
import ma.youcode.api.enums.UserType;
import ma.youcode.api.exceptions.ResourceNotFoundException;
import ma.youcode.api.models.Image;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.models.users.User;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.UserResponse;
import ma.youcode.api.repositories.ImageRepository;
import ma.youcode.api.repositories.UserRepository;
import ma.youcode.api.services.ImageService;
import ma.youcode.api.services.UserService;
import ma.youcode.api.utilities.factories.UserFactory;
import ma.youcode.api.utilities.mappers.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.mappers.GenericMapper;
import org.starter.utilities.repositories.GenericRepository;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final ImageService imageService;

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
        user.setActive(true);
        user.setIsEmailVerified(true);
        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);
    }

    @Override
    public UserResponse update(UUID uuid, UserRequest dto)  {
        return findAndExecute(uuid , user -> {
            userMapper.updateEntity(dto , user);
            return userMapper.toResponseDTO(user);
        });

    }

    @Override
    public void updatePhoto(UUID uuid, MultipartFile image) {
        findAndExecute(uuid , user -> {
            if (user.getPhotoUrl() != null){
                imageService.delete(user.getPhotoUrl());
            }
            user.setPhotoUrl(imageService.uploadImage(image));
        });
    }

    @Override
    public void disableAccount(UUID uuid) {
        findAndExecute(uuid , user -> {
            if (!user.getActive()){
                throw new IllegalArgumentException("Account is already locked");
            }
            user.setActive(false);
            userRepository.save(user);
        });
    }

    @Override
    public User findById(UUID uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    public void enableAccount(UUID uuid) {
        findAndExecute(uuid , user -> {
            if (user.getActive()){
                throw new IllegalArgumentException("Account is already active");
            }
            user.setActive(true);
            userRepository.save(user);
        });
    }

    @Override
    public void logout(UserSecurity user) {
        refreshTokenService.loadRefreshTokenByUserId(user.getId()).ifPresent(refreshTokenService::delete);
    }

    @Override
    public UserResponse readCurrentUser(@AuthUser UserSecurity user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .cin(user.getCin())
                .photoURL(user.getPhotoUrl())
                .role(user.getRole())
                .build();
    }
}
