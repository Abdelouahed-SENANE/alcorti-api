package ma.youcode.api.services.implementations;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.exceptions.ResourceNotFoundException;
import ma.youcode.api.models.vehicles.Vehicle;
import ma.youcode.api.payloads.requests.VehicleRequest;
import ma.youcode.api.payloads.responses.VehicleResponse;
import ma.youcode.api.repositories.VehicleRepository;
import ma.youcode.api.services.VehicleService;
import ma.youcode.api.utilities.mappers.VehicleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starter.utilities.mappers.GenericMapper;
import org.starter.utilities.repositories.GenericRepository;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public GenericMapper<Vehicle, VehicleResponse, VehicleRequest> getMapper() {
        return vehicleMapper;
    }

    @Override
    public Vehicle loadById(UUID uuid) {
        return vehicleRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found."));
    }

    @Override
    public GenericRepository<Vehicle, UUID> getRepository() {
        return vehicleRepository;
    }
}
