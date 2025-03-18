package ma.youcode.api.utilities.mappers;

import ma.youcode.api.models.vehicles.Vehicle;
import ma.youcode.api.payloads.requests.VehicleRequest;
import ma.youcode.api.payloads.responses.VehicleResponse;
import org.mapstruct.Mapper;
import org.starter.utilities.mappers.GenericMapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper extends GenericMapper<Vehicle, VehicleResponse, VehicleRequest> {

}
