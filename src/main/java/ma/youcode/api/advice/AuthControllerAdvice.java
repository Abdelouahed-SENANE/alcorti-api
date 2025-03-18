package ma.youcode.api.advice;

import ma.youcode.api.exceptions.auth.InvalidTokenRequestException;
import ma.youcode.api.exceptions.auth.RefreshTokenException;
import ma.youcode.api.exceptions.auth.UserLoginException;
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

    @ExceptionHandler(UserLoginException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<ErrorDTO> handleUserLoginException(UserLoginException e) {
        return error(HttpStatus.EXPECTATION_FAILED.value() , e.getMessage());
    }

    @ExceptionHandler(RefreshTokenException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<ErrorDTO> handleRefreshTokenException(RefreshTokenException e) {
        return error(HttpStatus.EXPECTATION_FAILED.value() , e.getMessage());
    }

}
