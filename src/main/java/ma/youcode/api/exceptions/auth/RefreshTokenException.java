package ma.youcode.api.exceptions.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class RefreshTokenException extends RuntimeException {

    public RefreshTokenException(String token , String message) {
        super(String.format("Couldn't refresh token for [%s]: [%s])", message, token));
    }
}
