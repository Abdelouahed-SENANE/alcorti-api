package ma.youcode.api.utilities.factories;

import ma.youcode.api.dtos.responses.UserResponseDTO;
import ma.youcode.api.entities.users.Admin;
import ma.youcode.api.entities.users.Customer;
import ma.youcode.api.entities.users.Driver;
import ma.youcode.api.entities.users.User;


public interface UserResponseDTOFactory {
    static UserResponseDTO build(User user) {
        if (user instanceof Admin admin) {
            return UserResponseDTO.builder()
                    .id(admin.getId())
                    .firstName(admin.getFirstName())
                    .lastName(admin.getLastName())
                    .email(admin.getEmail())
                    .cin(admin.getCin())
                    .picture(admin.getPicture())
                    .isEnabled(admin.getIsEnabled())
                    .isEmailVerified(admin.getIsEmailVerified())
                    .role(admin.getRole())
                    .build();
        } else if (user instanceof Customer customer) {
            return UserResponseDTO.builder()
                    .id(customer.getId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .email(customer.getEmail())
                    .cin(customer.getCin())
                    .isEnabled(customer.getIsEnabled())
                    .phoneNumber(customer.getPhoneNumber())
                    .isEmailVerified(customer.getIsEmailVerified())
                    .role(customer.getRole())
                    .build();
        } else if (user instanceof Driver driver) {
            return UserResponseDTO.builder()
                    .id(driver.getId())
                    .firstName(driver.getFirstName())
                    .lastName(driver.getLastName())
                    .email(driver.getEmail())
                    .isEnabled(driver.getIsEnabled())
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



