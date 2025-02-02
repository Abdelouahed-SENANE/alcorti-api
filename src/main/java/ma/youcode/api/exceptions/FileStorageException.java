package ma.youcode.api.exceptions;

public class FileStorageException extends  RuntimeException {

    public FileStorageException(String message , Throwable cause) {
        super(message, cause);
    }
}
