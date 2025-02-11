package ma.youcode.api.exceptions;

public class PostAccessDeniedException extends RuntimeException {
    public PostAccessDeniedException(String message) {
        super(message);
    }
}
