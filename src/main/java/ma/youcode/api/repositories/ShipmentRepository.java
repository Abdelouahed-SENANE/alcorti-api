package ma.youcode.api.repositories;

import ma.youcode.api.enums.ShipmentStatus;
import ma.youcode.api.models.shipments.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.starter.utilities.repositories.GenericRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ShipmentRepository extends GenericRepository<Shipment, UUID> , JpaSpecificationExecutor<Shipment> {

    Page<Shipment> findAllByCustomerId(Pageable pageable , UUID customerId);
    Page<Shipment> findAllByShipmentStatusAndCustomerId(Pageable pageable , ShipmentStatus shipmentStatus , UUID customerId);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT s.shipmentStatus , COUNT(s) FROM Shipment s GROUP BY s.shipmentStatus")
    List<Object[]> countShipmentsGroupedByStatus();
}
