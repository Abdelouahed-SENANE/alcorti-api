package ma.youcode.api.repositories;

import ma.youcode.api.models.shipments.ShipmentItem;
import org.starter.utilities.repositories.GenericRepository;

import java.util.UUID;

public interface ShipmentItemRepository extends GenericRepository<ShipmentItem, UUID> {
}
