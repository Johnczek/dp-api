package cz.johnczek.dpapi.core.errorhandling.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception for {@link HttpStatus#INTERNAL_SERVER_ERROR}. Every exception inheriting from this will be sent as HTTP
 * request with code 500 with given message identifier and possibly with message arguments.
 */
public class BaseInternalServerErrorRestException extends BaseRestException {

    public BaseInternalServerErrorRestException(String message) {
        super(message);
    }

    public BaseInternalServerErrorRestException(String message, Object[] args) {
        super(message, args);
    }

    public BaseInternalServerErrorRestException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public BaseInternalServerErrorRestException(String message, Throwable cause) {
        super(message, cause);
    }
}
