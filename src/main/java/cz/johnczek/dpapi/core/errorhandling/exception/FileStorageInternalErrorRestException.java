package cz.johnczek.dpapi.core.errorhandling.exception;

public class FileStorageInternalErrorRestException extends BaseInternalServerErrorRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.file.internalError";

    public FileStorageInternalErrorRestException() {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{});
    }

    public FileStorageInternalErrorRestException(Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{});
    }
}
