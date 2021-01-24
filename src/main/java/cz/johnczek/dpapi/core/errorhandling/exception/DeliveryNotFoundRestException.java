package cz.johnczek.dpapi.core.errorhandling.exception;

public class DeliveryNotFoundRestException extends BaseNotFoundRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.delivery.notFound";

    public DeliveryNotFoundRestException(long id) {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{id});
    }

    public DeliveryNotFoundRestException(long id, Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{id});
    }
}
