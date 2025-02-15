package ma.youcode.api.exceptions;

public class ShipmentAccessDeniedException extends RuntimeException {
    public ShipmentAccessDeniedException(String message) {
        super(message);
    }
}
