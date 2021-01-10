package cz.johnczek.dpapi.core.errorhandling.exception;

public class BaseForbiddenRestException extends BaseRestException {

    private static final String EXCEPTION_DEFAULT_MESSAGE_CODE = "error.forbidden";

    public BaseForbiddenRestException() {
        super(EXCEPTION_DEFAULT_MESSAGE_CODE);
    }

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

