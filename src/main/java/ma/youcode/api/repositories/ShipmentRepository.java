package ma.youcode.api.repositories;

import ma.youcode.api.models.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.starter.utilities.repositories.GenericRepository;

import java.util.UUID;

public interface ShipmentRepository extends GenericRepository<Shipment, UUID> {

    Page<Shipment> findAllByCustomerId(Pageable pageable , UUID customerId);
}
