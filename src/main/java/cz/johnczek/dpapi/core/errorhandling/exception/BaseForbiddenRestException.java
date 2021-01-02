package cz.johnczek.dpapi.core.errorhandling.exception;

public class BaseForbiddenRestException extends BaseRestException {

    public BaseForbiddenRestException(String message) {
        super(message);
    }

    public BaseForbiddenRestException(String message, Object[] args) {
        super(message, args);
    }

    public BaseForbiddenRestException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public BaseForbiddenRestException(String message, Throwable cause) {
        super(message, cause);
    }
}

