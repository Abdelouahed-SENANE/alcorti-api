package ma.youcode.api.utilities.factories;

import ma.youcode.api.enums.UserType;
import ma.youcode.api.models.users.Admin;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.Driver;
import ma.youcode.api.models.users.User;
import ma.youcode.api.models.vehicles.Vehicle;
import ma.youcode.api.models.vehicles.VehicleOfDriver;
import ma.youcode.api.payloads.requests.UserRequest;

import java.util.stream.Collectors;

public interface UserFactory {
    static User build(UserRequest dto) {
            return switch (dto.userType()) {
                case DRIVER -> Driver.builder()
                        .firstName(dto.firstName())
                        .lastName(dto.lastName())
                        .email(dto.email())
                        .password(dto.password())
                        .phoneNumber(dto.phoneNumber())
                        .active(true)
                        .isEmailVerified(true)
                        .cin(dto.cin())
                        .build();
                case CUSTOMER -> Customer.builder()
                        .firstName(dto.firstName())
                        .lastName(dto.lastName())
                        .email(dto.email())
                        .active(true)
                        .isEmailVerified(true)
                        .password(dto.password())
                        .cin(dto.cin())
                        .phoneNumber(dto.phoneNumber())
                        .build();
                default -> throw new UnsupportedOperationException("Unknown user type: " + dto.userType());
            };
        
    }
}
