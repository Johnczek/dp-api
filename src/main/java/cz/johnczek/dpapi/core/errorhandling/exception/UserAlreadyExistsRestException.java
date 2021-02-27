package cz.johnczek.dpapi.core.errorhandling.exception;

public class UserAlreadyExistsRestException extends BaseBadRequestRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.user.alreadyExists";

    public UserAlreadyExistsRestException() {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{});
    }

    public UserAlreadyExistsRestException(Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{});
    }
}
