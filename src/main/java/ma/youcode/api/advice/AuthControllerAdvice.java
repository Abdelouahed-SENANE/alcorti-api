package ma.youcode.api.advice;

import ma.youcode.api.exceptions.InvalidTokenRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.starter.utilities.dtos.ErrorDTO;
import org.starter.utilities.exceptions.AbstractGlobalExceptionHandler;

import static org.starter.utilities.response.Response.error;

@RestControllerAdvice
public class AuthControllerAdvice extends AbstractGlobalExceptionHandler {


    @ExceptionHandler(InvalidTokenRequestException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<ErrorDTO> handleInvalidTokenRequestException(InvalidTokenRequestException e) {
        return error(HttpStatus.NOT_ACCEPTABLE.value() , e.getMessage());
    }

}
