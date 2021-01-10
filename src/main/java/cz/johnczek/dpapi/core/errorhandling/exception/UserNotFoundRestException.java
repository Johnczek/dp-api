package cz.johnczek.dpapi.core.errorhandling.exception;

public class UserNotFoundRestException extends BaseNotFoundRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.user.notFoundOrIncorrectPassword";

    public UserNotFoundRestException(long id) {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{id});
    }

    public UserNotFoundRestException(long id, Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{id});
    }
}
