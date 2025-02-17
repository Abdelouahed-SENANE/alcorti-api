package ma.youcode.api.controllers;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.models.vehicles.Vehicle;
import ma.youcode.api.payloads.requests.VehicleRequest;
import ma.youcode.api.payloads.responses.VehicleResponse;
import ma.youcode.api.services.VehicleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starter.utilities.controllers.CrudController;
import org.starter.utilities.services.CrudService;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController implements CrudController<VehicleResponse, VehicleRequest, Vehicle, UUID> {

    private final VehicleService vehicleService;

    @Override
    public CrudService<VehicleResponse, VehicleRequest, Vehicle, UUID> getService() {
        return vehicleService;
    }

    @Override
    public Class<Vehicle> getEntityClass() {
        return Vehicle.class;
    }
}
