package ma.youcode.api.utilities.mappers;

import ma.youcode.api.models.shipments.Shipment;
import ma.youcode.api.models.vehicles.Vehicle;
import ma.youcode.api.payloads.requests.ShipmentRequest;
import ma.youcode.api.payloads.requests.VehicleRequest;
import ma.youcode.api.payloads.responses.ShipmentResponse;
import ma.youcode.api.payloads.responses.VehicleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.starter.utilities.mappers.GenericMapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper extends GenericMapper<Vehicle, VehicleResponse, VehicleRequest> {

}
