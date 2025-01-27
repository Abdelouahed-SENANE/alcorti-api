package ma.youcode.api.utilities.mappers;


import ma.youcode.api.dtos.requests.UserRequestDTO;
import ma.youcode.api.dtos.responses.UserResponseDTO;
import ma.youcode.api.entities.users.Admin;
import ma.youcode.api.entities.users.Customer;
import ma.youcode.api.entities.users.Driver;
import ma.youcode.api.entities.users.User;
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
