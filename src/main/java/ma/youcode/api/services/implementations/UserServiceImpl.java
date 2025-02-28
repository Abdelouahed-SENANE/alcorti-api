package ma.youcode.api.services.implementations;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.annotations.AuthUser;
import ma.youcode.api.enums.UserType;
import ma.youcode.api.exceptions.ResourceNotFoundException;
import ma.youcode.api.models.Image;
import ma.youcode.api.models.users.Driver;
import ma.youcode.api.models.users.User;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.models.vehicles.Vehicle;
import ma.youcode.api.models.vehicles.VehicleOfDriver;
import ma.youcode.api.payloads.requests.DriverCompleteRequest;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.requests.VehicleOfDriverRequest;
import ma.youcode.api.payloads.responses.UserResponse;
import ma.youcode.api.repositories.UserRepository;
import ma.youcode.api.services.ImageService;
import ma.youcode.api.services.UserService;
import ma.youcode.api.services.VehicleService;
import ma.youcode.api.utilities.factories.UserFactory;
import ma.youcode.api.utilities.factories.UserResponseFactory;
import ma.youcode.api.utilities.mappers.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.mappers.GenericMapper;
import org.starter.utilities.repositories.GenericRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final ImageService imageService;
    private final VehicleService vehicleService;

    @Override
    public GenericRepository<User, UUID> getRepository() {
        return userRepository;
    }

    @Override
    public GenericMapper<User, UserResponse, UserRequest> getMapper() {
        return userMapper;
    }

    @Override
    public void create(UserRequest dto) {
        User user = UserFactory.build(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));

        userRepository.save(user);
    }


    @Override
    public UserResponse update(UUID uuid, UserRequest dto) {
        return findAndExecute(uuid, user -> {
            userMapper.updateEntity(dto, user);
            return userMapper.toResponseDTO(user);
        });

    }

    @Override
    public void updatePhoto(UUID uuid, MultipartFile image) {
        findAndExecute(uuid, user -> {
            if (user.getPhotoUrl() != null) {
                imageService.delete(user.getPhotoUrl());
            }
            user.setPhotoUrl(imageService.uploadImage(image));
        });
    }

    @Override
    public void disableAccount(UUID uuid) {
        findAndExecute(uuid, user -> {
            if (!user.getActive()) {
                throw new IllegalArgumentException("Account is already locked");
            }
            user.setActive(false);
            userRepository.save(user);
        });
    }

    @Override
    public User findById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    public void enableAccount(UUID uuid) {
        findAndExecute(uuid, user -> {
            if (user.getActive()) {
                throw new IllegalArgumentException("Account is already active");
            }
            user.setActive(true);
            userRepository.save(user);
        });
    }

    @Override
    public void logout(UserSecurity user) {
        refreshTokenService.loadRefreshTokenByUserCin(user.getCin()).ifPresent(refreshTokenService::delete);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmailOrCin(email).isPresent();
    }

    @Override
    public boolean cinExists(String cin) {
        return userRepository.findByEmailOrCin(cin).isPresent();
    }

    @Override
    public void finalizeDriverRegistration(DriverCompleteRequest dto) {

        Driver driver = (Driver) findById(getAuthUserId());
        updateDriverProfile(driver, dto.vehicles());
        driver.setLocation(dto.location());
        driver.setProfileCompleted(true);
        userRepository.save(driver);
    }

    private UUID getAuthUserId() {
        return ((UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    private void updateDriverProfile(Driver driver, List<VehicleOfDriverRequest> vehicles) {
        driver.getVehiclesOfDriver().clear();

        driver.getVehiclesOfDriver().addAll(
                vehicles.stream()
                        .map(this::createVehicleOfDriver)
                        .collect(Collectors.toSet()));
    }

    private VehicleOfDriver createVehicleOfDriver(VehicleOfDriverRequest vehicleOfDriver) {
        Vehicle vehicle = vehicleService.loadById(vehicleOfDriver.vehicleId());
        String imageUrl = imageService.uploadImage(vehicleOfDriver.image());
        return VehicleOfDriver.builder()
                .licensePlate(vehicleOfDriver.licensePlate())
                .imageUrl(imageUrl)
                .vehicle(vehicle)
                .driver((Driver) findById(getAuthUserId()))
                .build();
    }
}
