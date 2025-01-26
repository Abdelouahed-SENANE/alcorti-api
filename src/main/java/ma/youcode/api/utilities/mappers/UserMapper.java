package ma.youcode.api.utilities.mappers;


import ma.youcode.api.dtos.requests.UserRequestDTO;
import ma.youcode.api.dtos.responses.UserResponseDTO;
import ma.youcode.api.entities.users.Admin;
import ma.youcode.api.entities.users.Customer;
import ma.youcode.api.entities.users.Driver;
import ma.youcode.api.entities.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.starter.utilities.mappers.GenericMapper;

@Mapper(componentModel = "spring")
public interface UserMapper  extends GenericMapper<User , UserResponseDTO , UserRequestDTO> {

    UserResponseDTO toResponseDTO(Admin admin);
    @Mapping(target = "phoneNumber", source = "customer.phoneNumber")
    UserResponseDTO toResponseDTO(Customer customer);
    @Mapping(target = "phoneNumber", source = "driver.phoneNumber")
    @Mapping(target = "coordinates", source = "driver.coordinates")
    UserResponseDTO toResponseDTO(Driver driver);

    default UserResponseDTO toResponseDTO(User user) {
        if (user instanceof Admin) {
            return toResponseDTO((Admin) user);
        } else if (user instanceof Customer) {
            return toResponseDTO((Customer) user);
        } else if (user instanceof Driver) {
            return toResponseDTO((Driver) user);
        }
        throw new IllegalArgumentException("Unknown user type: " + user.getClass().getName());
    }
}
