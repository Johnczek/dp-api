package cz.johnczek.dpapi.core.errorhandling.exception;

public class NotEnoughAmountBidException extends BaseBadRequestRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.item.bid.notEnoughAmount";

    public NotEnoughAmountBidException() {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{});
    }

    public NotEnoughAmountBidException(Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{});
    }
}
