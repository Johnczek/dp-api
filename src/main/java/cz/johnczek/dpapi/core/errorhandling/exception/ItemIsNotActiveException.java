package cz.johnczek.dpapi.core.errorhandling.exception;

public class ItemIsNotActiveException extends BaseBadRequestRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.item.bid.notActive";

    public ItemIsNotActiveException() {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{});
    }

    public ItemIsNotActiveException(Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{});
    }
}
