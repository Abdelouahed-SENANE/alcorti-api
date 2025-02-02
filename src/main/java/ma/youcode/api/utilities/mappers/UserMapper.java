package ma.youcode.api.utilities.mappers;


import ma.youcode.api.models.users.Admin;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.Driver;
import ma.youcode.api.models.users.User;
import ma.youcode.api.payloads.requests.UserRequest;
import ma.youcode.api.payloads.responses.UserResponse;
import ma.youcode.api.utilities.FileServiceStorage;
import ma.youcode.api.utilities.factories.UserResponseDTOFactory;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.mappers.GenericMapper;

@Mapper(componentModel = "spring")
public interface UserMapper  extends GenericMapper<User , UserResponse, UserRequest> {

//    UserResponse toResponseDTO(Admin admin);
//    UserResponse toResponseDTO(Customer customer);
//    UserResponse toResponseDTO(Driver driver);

    @Mapping(source = "photo" , target = "photoURL" , qualifiedByName = "uploadPicture")
    User fromRequestDTO(UserRequest dto);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(source = "photo" , target = "photoURL" , qualifiedByName = "uploadPicture")
    void updateEntity(UserRequest dto, @MappingTarget User user);

    default UserResponse toResponseDTO(User user) {
        return UserResponseDTOFactory.build(user);
    }

    @Named("uploadPicture")
    default String uploadPhotoIfExists(MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            return null;
        }
        return FileServiceStorage.store(photo);
    }

}
