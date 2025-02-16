package ma.youcode.api.utilities.mappers;

import ma.youcode.api.models.ShipmentItem;
import ma.youcode.api.payloads.requests.ShipmentItemRequest;
import ma.youcode.api.payloads.responses.ShipmentItemResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.starter.utilities.mappers.GenericMapper;

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