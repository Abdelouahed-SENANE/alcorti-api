package ma.youcode.api.utilities.mappers;


import ma.youcode.api.payload.requests.UserRequestDTO;
import ma.youcode.api.payload.responses.UserResponseDTO;
import ma.youcode.api.models.users.Admin;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.Driver;
import ma.youcode.api.models.users.User;
import ma.youcode.api.utilities.factories.UserResponseDTOFactory;
import org.mapstruct.*;
import org.starter.utilities.mappers.GenericMapper;

@Mapper(componentModel = "spring")
public interface UserMapper  extends GenericMapper<User , UserResponseDTO , UserRequestDTO> {

    UserResponseDTO toResponseDTO(Admin admin);
    UserResponseDTO toResponseDTO(Customer customer);
    UserResponseDTO toResponseDTO(Driver driver);

//    @Mapping(target = "picture" , ignore = true)
    User fromRequestDTO(UserRequestDTO dto);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
//    @Mapping(target = "picture" , ignore = true)
    void updateEntity(UserRequestDTO dto, @MappingTarget User user);

    default UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTOFactory.build(user);
    }


}
