package ma.youcode.api.utilities.factories.implementations;

import ma.youcode.api.constants.UserType;
import ma.youcode.api.dtos.requests.UserRequestDTO;
import ma.youcode.api.entities.users.Admin;
import ma.youcode.api.entities.users.Customer;
import ma.youcode.api.entities.users.Driver;
import ma.youcode.api.entities.users.User;
import ma.youcode.api.utilities.factories.UserFactory;
import org.springframework.stereotype.Component;

@Component
public class UserFactoryImpl implements UserFactory {

    @Override
    public User build(UserRequestDTO userDto , UserType userType) {
        return switch (userType) {
            case ADMIN -> Admin.builder()
                    .firstName(userDto.firstName())
                    .lastName(userDto.lastName())
                    .email(userDto.email())
                    .cin(userDto.cin())
                    .password(userDto.password())
                    .picture(userDto.picture())
                    .isActive(true)
                    .isEmailVerified(true)
                    .build();
            case DRIVER -> Driver.builder()
                    .firstName(userDto.firstName())
                    .lastName(userDto.lastName())
                    .email(userDto.email())
                    .password(userDto.password())
                    .picture(userDto.picture())
                    .phoneNumber(userDto.phoneNumber())
                    .isActive(false)
                    .isEmailVerified(false)
                    .cin(userDto.cin())
                    .coordinates(userDto.coordinates())
                    .build();
            case CUSTOMER -> Customer.builder()
                    .firstName(userDto.firstName())
                    .lastName(userDto.lastName())
                    .email(userDto.email())
                    .password(userDto.password())
                    .picture(userDto.picture())
                    .cin(userDto.cin())
                    .phoneNumber(userDto.phoneNumber())
                    .build();
            default -> throw new UnsupportedOperationException("Unknown user type: " + userType);
        };
    }
}
