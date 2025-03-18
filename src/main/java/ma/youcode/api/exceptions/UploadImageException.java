package ma.youcode.api.exceptions;

public class UploadImageException extends  RuntimeException {

    public UploadImageException(String message , Throwable cause) {
        super(message, cause);
    }
}
