package ma.youcode.api.exceptions;

public class InvalidShipmentStateException extends RuntimeException {
    public InvalidShipmentStateException(String message) {
        super(message);
    }
}
