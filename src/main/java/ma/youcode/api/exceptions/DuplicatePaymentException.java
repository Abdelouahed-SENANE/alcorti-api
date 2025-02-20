package ma.youcode.api.exceptions;

public class DuplicatePaymentException extends  RuntimeException {

    public DuplicatePaymentException(String message , Throwable cause) {
        super(message, cause);
    }
    public DuplicatePaymentException(String message) {
        super(message);
    }
}
