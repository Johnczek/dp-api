package cz.johnczek.dpapi.core.errorhandling.exception;

import cz.johnczek.dpapi.core.errorhandling.handler.RestExceptionHandler;
import lombok.Getter;

/**
 * Ancestor for all execeptions thrown for ajax requests catched in {@link RestExceptionHandler}.
 */

@Getter
public class BaseRestException extends RuntimeException {

    private final transient Object[] args;

    public BaseRestException(String message) {
        super(message);
        this.args = new Object[]{};
    }

    public BaseRestException(String message, Object[] args) {
        super(message);
        this.args = args;
    }

    public BaseRestException(String message, Throwable cause, Object[] args) {
        super(message, cause);
        this.args = args;
    }

    public BaseRestException(String message, Throwable cause) {
        super(message, cause);
        this.args = new Object[]{};
    }
}

