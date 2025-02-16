package ma.youcode.api.utilities.mappers;


import ma.youcode.api.models.users.User;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.UserResponse;
import ma.youcode.api.utilities.factories.UserResponseDTOFactory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.starter.utilities.mappers.GenericMapper;

@Mapper(componentModel = "spring")
public interface UserMapper  extends GenericMapper<User , UserResponse, UserRequest> {

    User fromRequestDTO(UserRequest dto);

    @Override
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(UserRequest dto, @MappingTarget User user);

    default UserResponse toResponseDTO(User user) {
        return UserResponseDTOFactory.build(user);
    }



}
