package ma.youcode.api.repositories;

import ma.youcode.api.models.vehicles.Vehicle;
import org.starter.utilities.repositories.GenericRepository;

import java.util.UUID;

public interface VehicleRepository extends GenericRepository<Vehicle , UUID> {
}
