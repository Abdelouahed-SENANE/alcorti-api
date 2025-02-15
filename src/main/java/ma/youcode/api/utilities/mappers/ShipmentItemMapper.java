package ma.youcode.api.utilities.mappers;

import ma.youcode.api.models.ShipmentItem;
import ma.youcode.api.payloads.requests.ShipmentItemRequest;
import ma.youcode.api.payloads.responses.ShipmentItemResponse;
import ma.youcode.api.utilities.FileServiceStorage;
import ma.youcode.api.utilities.shared.Dimensions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.mappers.GenericMapper;

@Mapper(componentModel = "spring")
public interface ShipmentItemMapper extends GenericMapper<ShipmentItem, ShipmentItemResponse, ShipmentItemRequest> {

    @Override
    @Mapping(target = "imageURL", source = "image" , qualifiedByName = "uploadImage")
    @Mapping(target = "volume", source = "dimensions", qualifiedByName = "calculateVolume")
    ShipmentItem fromRequestDTO(ShipmentItemRequest dto);

    @Named("calculateVolume")
    default Double calculateVolume(Dimensions dimensions) {
        if (dimensions == null) {
            return 0.0;
        }
        return dimensions.length() * dimensions.width() * dimensions.height();
    }

    @Named("uploadImage")
    default String uploadImageIfExists(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null;
        }
        return FileServiceStorage.store(imageFile);
    }
}
