package cz.johnczek.dpapi.core.errorhandling.exception;

import lombok.NonNull;

public class FileNotFoundRestException extends BaseNotFoundRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.file.notFound";

    public FileNotFoundRestException(@NonNull String uuid) {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{uuid});
    }

    public FileNotFoundRestException(@NonNull String uuid, Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{uuid});
    }
}
