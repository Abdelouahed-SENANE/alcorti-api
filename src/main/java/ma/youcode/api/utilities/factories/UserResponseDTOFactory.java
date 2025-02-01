package ma.youcode.api.utilities.factories;

import ma.youcode.api.models.users.Admin;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.Driver;
import ma.youcode.api.models.users.User;
import ma.youcode.api.payloads.responses.UserResponse;


public interface UserResponseDTOFactory {
    static UserResponse build(User user) {
        if (user instanceof Admin admin) {
            return UserResponse.builder()
                    .id(admin.getId())
                    .firstName(admin.getFirstName())
                    .lastName(admin.getLastName())
                    .email(admin.getEmail())
                    .cin(admin.getCin())
                    .picture(admin.getPicture())
                    .isAccountNonLocked(admin.getActive())
                    .isEmailVerified(admin.getIsEmailVerified())
                    .role(admin.getRole())
                    .build();
        } else if (user instanceof Customer customer) {
            return UserResponse.builder()
                    .id(customer.getId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .email(customer.getEmail())
                    .cin(customer.getCin())
                    .isAccountNonLocked(customer.getActive())
                    .phoneNumber(customer.getPhoneNumber())
                    .isEmailVerified(customer.getIsEmailVerified())
                    .role(customer.getRole())
                    .build();
        } else if (user instanceof Driver driver) {
            return UserResponse.builder()
                    .id(driver.getId())
                    .firstName(driver.getFirstName())
                    .lastName(driver.getLastName())
                    .email(driver.getEmail())
                    .isAccountNonLocked(driver.getActive())
                    .phoneNumber(driver.getPhoneNumber())
                    .isEmailVerified(driver.getIsEmailVerified())
                    .role(driver.getRole())
                    .picture(driver.getPicture())
                    .coordinates(driver.getCoordinates())
                    .build();

        } else {
            throw new UnsupportedOperationException("Unknown user type: " + user.getClass().getSimpleName());
        }

    }
}



