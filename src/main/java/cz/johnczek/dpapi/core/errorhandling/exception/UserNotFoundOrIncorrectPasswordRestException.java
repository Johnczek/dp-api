package cz.johnczek.dpapi.core.errorhandling.exception;

public class UserNotFoundOrIncorrectPasswordRestException extends BaseNotFoundRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.user.notFoundOrIncorrectPassword";

    public UserNotFoundOrIncorrectPasswordRestException() {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{});
    }

    public UserNotFoundOrIncorrectPasswordRestException(Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{});
    }
}
