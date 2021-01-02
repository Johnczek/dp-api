package cz.johnczek.dpapi.core.errorhandling.exception;

public class UserNotFoundRestException extends BaseNotFoundRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.user.notFound";

    public UserNotFoundRestException() {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{});
    }

    public UserNotFoundRestException(Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{});
    }
}
