package ma.youcode.api.exceptions;

public class UnauthorizedShipmentAccessException extends RuntimeException {
    public UnauthorizedShipmentAccessException(String message) {
        super(message);
    }
}
