package ma.youcode.api.utilities.factories;

import ma.youcode.api.constants.UserType;
import ma.youcode.api.dtos.requests.UserRequestDTO;
import ma.youcode.api.entities.users.Admin;
import ma.youcode.api.entities.users.Customer;
import ma.youcode.api.entities.users.Driver;
import ma.youcode.api.entities.users.User;

public interface UserFactory {
    static User build(UserRequestDTO dto, UserType userType) {
            return switch (userType) {
                case ADMIN -> Admin.builder()
                        .firstName(dto.firstName())
                        .lastName(dto.lastName())
                        .email(dto.email())
                        .cin(dto.cin())
                        .password(dto.password())
                        .isAccountNonLocked(true)
                        .isEmailVerified(true)
                        .build();
                case DRIVER -> Driver.builder()
                        .firstName(dto.firstName())
                        .lastName(dto.lastName())
                        .email(dto.email())
                        .password(dto.password())
                        .phoneNumber(dto.phoneNumber())
                        .isAccountNonLocked(false)
                        .isEmailVerified(false)
                        .cin(dto.cin())
                        .coordinates(dto.coordinates())
                        .build();
                case CUSTOMER -> Customer.builder()
                        .firstName(dto.firstName())
                        .lastName(dto.lastName())
                        .email(dto.email())
                        .password(dto.password())
                        .cin(dto.cin())
                        .phoneNumber(dto.phoneNumber())
                        .build();
                default -> throw new UnsupportedOperationException("Unknown user type: " + userType);
            };
        
    }
}
