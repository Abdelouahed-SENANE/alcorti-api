package ma.youcode.api.services;

import ma.youcode.api.models.vehicles.Vehicle;
import ma.youcode.api.payloads.requests.VehicleRequest;
import ma.youcode.api.payloads.responses.VehicleResponse;
import org.starter.utilities.services.CrudService;

import java.util.UUID;

public interface VehicleService extends CrudService<VehicleResponse , VehicleRequest , Vehicle , UUID> {

    Vehicle loadById(UUID uuid);
}
