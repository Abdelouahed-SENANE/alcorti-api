package ma.youcode.api.utilities.factories;

import ma.youcode.api.constants.UserType;
import ma.youcode.api.models.users.Admin;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.Driver;
import ma.youcode.api.models.users.User;
import ma.youcode.api.payloads.requests.UserRequest;

public interface UserFactory {
    static User build(UserRequest dto, UserType userType) {
            return switch (userType) {
                case ADMIN -> Admin.builder()
                        .firstName(dto.firstName())
                        .lastName(dto.lastName())
                        .email(dto.email())
                        .cin(dto.cin())
                        .password(dto.password())
                        .active(true)
                        .isEmailVerified(true)
                        .build();
                case DRIVER -> Driver.builder()
                        .firstName(dto.firstName())
                        .lastName(dto.lastName())
                        .email(dto.email())
                        .password(dto.password())
                        .phoneNumber(dto.phoneNumber())
                        .active(true)
                        .isEmailVerified(false)
                        .cin(dto.cin())
                        .coordinates(dto.coordinates())
                        .build();
                case CUSTOMER -> Customer.builder()
                        .firstName(dto.firstName())
                        .lastName(dto.lastName())
                        .email(dto.email())
                        .active(true)
                        .password(dto.password())
                        .cin(dto.cin())
                        .phoneNumber(dto.phoneNumber())
                        .build();
                default -> throw new UnsupportedOperationException("Unknown user type: " + userType);
            };
        
    }
}
