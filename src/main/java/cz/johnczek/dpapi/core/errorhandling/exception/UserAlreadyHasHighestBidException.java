package cz.johnczek.dpapi.core.errorhandling.exception;

public class UserAlreadyHasHighestBidException extends BaseBadRequestRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.item.bid.hasAlreadyHighestBid";

    public UserAlreadyHasHighestBidException() {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{});
    }

    public UserAlreadyHasHighestBidException(Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{});
    }
}
