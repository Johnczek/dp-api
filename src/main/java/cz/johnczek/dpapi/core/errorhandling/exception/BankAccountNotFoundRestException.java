package cz.johnczek.dpapi.core.errorhandling.exception;

public class BankAccountNotFoundRestException extends BaseNotFoundRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.bankAccount.notFound";

    public BankAccountNotFoundRestException(long id) {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{id});
    }

    public BankAccountNotFoundRestException(long id, Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{id});
    }
}
