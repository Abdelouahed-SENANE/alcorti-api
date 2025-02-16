package ma.youcode.api.advice;

import ma.youcode.api.exceptions.InvalidShipmentStateException;
import ma.youcode.api.exceptions.UnauthorizedShipmentAccessException;
import ma.youcode.api.exceptions.UploadImageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.starter.utilities.dtos.ErrorDTO;
import org.starter.utilities.exceptions.AbstractGlobalExceptionHandler;

import static org.starter.utilities.response.Response.error;

@RestControllerAdvice
public class GlobalControllerAdvice extends AbstractGlobalExceptionHandler {

    @ExceptionHandler(UploadImageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleUploadImageException(UploadImageException e) {
        return error(HttpStatus.BAD_REQUEST.value() , e.getMessage());
    }

    @ExceptionHandler(InvalidShipmentStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleInvalidShipmentStateException(InvalidShipmentStateException e) {
        return error(HttpStatus.BAD_REQUEST.value() , e.getMessage());
    }

    @ExceptionHandler(UnauthorizedShipmentAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorDTO> handleUnauthorizedShipmentAccessException(InvalidShipmentStateException e) {
        return error(HttpStatus.FORBIDDEN.value() , e.getMessage());
    }

}
