package cz.johnczek.dpapi.core.errorhandling.exception;

public class ItemNotBuyableRestException extends BaseRestException {

    private static final String EXCEPTION_DEFAULT_MESSAGE_CODE = "error.item.notBuyable";

    public ItemNotBuyableRestException() {
        super(EXCEPTION_DEFAULT_MESSAGE_CODE);
    }

    public ItemNotBuyableRestException(String message) {
        super(message);
    }

    public ItemNotBuyableRestException(String message, Object[] args) {
        super(message, args);
    }

    public ItemNotBuyableRestException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public ItemNotBuyableRestException(String message, Throwable cause) {
        super(message, cause);
    }
}

