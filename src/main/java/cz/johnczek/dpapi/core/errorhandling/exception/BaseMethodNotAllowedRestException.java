package cz.johnczek.dpapi.core.errorhandling.exception;

public class BaseMethodNotAllowedRestException extends BaseRestException {

    public BaseMethodNotAllowedRestException(String message) {
        super(message);
    }

    public BaseMethodNotAllowedRestException(String message, Object[] args) {
        super(message, args);
    }

    public BaseMethodNotAllowedRestException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public BaseMethodNotAllowedRestException(String message, Throwable cause) {
        super(message, cause);
    }
}
