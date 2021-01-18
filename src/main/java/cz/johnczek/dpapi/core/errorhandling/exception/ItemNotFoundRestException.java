package cz.johnczek.dpapi.core.errorhandling.exception;

public class ItemNotFoundRestException extends BaseNotFoundRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.item.notFound";

    public ItemNotFoundRestException(long id) {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{id});
    }

    public ItemNotFoundRestException(long id, Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{id});
    }
}
