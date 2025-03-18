package ma.youcode.api.exceptions;

public class DriverNotAllowedException extends RuntimeException {
    public DriverNotAllowedException(String message) {
        super(message);
    }
}
