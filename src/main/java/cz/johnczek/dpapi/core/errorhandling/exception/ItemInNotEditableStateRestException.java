package cz.johnczek.dpapi.core.errorhandling.exception;

public class ItemInNotEditableStateRestException extends BaseBadRequestRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.item.notEditable";

    public ItemInNotEditableStateRestException() {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{});
    }

    public ItemInNotEditableStateRestException(Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{});
    }
}
