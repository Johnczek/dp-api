package cz.johnczek.dpapi.core.errorhandling.exception;

public class PaymentNotFoundRestException extends BaseNotFoundRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.payment.notFound";

    public PaymentNotFoundRestException(long id) {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{id});
    }

    public PaymentNotFoundRestException(long id, Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{id});
    }
}
