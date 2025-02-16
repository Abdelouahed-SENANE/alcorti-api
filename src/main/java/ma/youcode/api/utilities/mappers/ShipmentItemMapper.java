package ma.youcode.api.utilities.mappers;

import ma.youcode.api.models.ShipmentItem;
import ma.youcode.api.payloads.requests.ShipmentItemRequest;
import ma.youcode.api.payloads.responses.ShipmentItemResponse;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.mappers.GenericMapper;

import java.io.IOException;

@Mapper(componentModel = "spring")
public interface ShipmentItemMapper extends GenericMapper<ShipmentItem, ShipmentItemResponse, ShipmentItemRequest> {

    @Override
    ShipmentItem fromRequestDTO(ShipmentItemRequest dto);

    @Override
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void updateEntity(ShipmentItemRequest dto, @MappingTarget ShipmentItem entity);

}