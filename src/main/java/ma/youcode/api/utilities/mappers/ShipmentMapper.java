package ma.youcode.api.utilities.mappers;

import ma.youcode.api.models.Shipment;
import ma.youcode.api.payloads.requests.ShipmentRequest;
import ma.youcode.api.payloads.responses.ShipmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.starter.utilities.mappers.GenericMapper;

@Mapper(componentModel = "spring" , uses = ShipmentItemMapper.class)
public interface ShipmentMapper extends GenericMapper<Shipment, ShipmentResponse, ShipmentRequest> {

    @Override
    @Mapping(target = "customer", ignore = true)
    Shipment fromRequestDTO(ShipmentRequest dto);
}
