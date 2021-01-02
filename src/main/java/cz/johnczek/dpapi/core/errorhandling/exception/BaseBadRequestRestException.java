package cz.johnczek.dpapi.core.errorhandling.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception for {@link HttpStatus#BAD_REQUEST}. Every exception inheriting from this will be sent as HTTP
 * request with code 400 with given message identifier and possibly with message arguments.
 */
public class BaseBadRequestRestException extends BaseRestException {

    public BaseBadRequestRestException(String message) {
        super(message);
    }

    public BaseBadRequestRestException(String message, Object[] args) {
        super(message, args);
    }

    public BaseBadRequestRestException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public BaseBadRequestRestException(String message, Throwable cause) {
        super(message, cause);
    }
}
