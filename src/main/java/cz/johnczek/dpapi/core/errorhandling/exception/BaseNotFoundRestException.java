package cz.johnczek.dpapi.core.errorhandling.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception for {@link HttpStatus#NOT_FOUND}. Every exception inheriting from this will be sent as HTTP
 * request with code 404 with given message identifier and possibly with message arguments.
 */
public class BaseNotFoundRestException extends BaseRestException {

    public BaseNotFoundRestException(String message) {
        super(message);
    }

    public BaseNotFoundRestException(String message, Object[] args) {
        super(message, args);
    }

    public BaseNotFoundRestException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public BaseNotFoundRestException(String message, Throwable cause) {
        super(message, cause);
    }
}
