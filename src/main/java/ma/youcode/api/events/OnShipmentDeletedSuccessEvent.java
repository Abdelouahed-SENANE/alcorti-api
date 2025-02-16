package ma.youcode.api.events;

import lombok.Getter;
import ma.youcode.api.models.Shipment;
import org.springframework.context.ApplicationEvent;


@Getter
public class OnShipmentDeletedSuccessEvent extends ApplicationEvent {
    private final Shipment shipment;

    public OnShipmentDeletedSuccessEvent(Object source , Shipment shipment) {
        super(source);
        this.shipment = shipment;
    }
}
