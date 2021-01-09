package cz.johnczek.dpapi.core.errorhandling.exception;

import lombok.NonNull;

public class RoleNotFoundRestException extends BaseInternalServerErrorRestException {

    private static final String EXCEPTION_MESSAGE_CODE = "error.role.notFound";

    public RoleNotFoundRestException(@NonNull String roleCode) {
        super(EXCEPTION_MESSAGE_CODE, new Object[]{roleCode});
    }

    public RoleNotFoundRestException(@NonNull String roleCode, Throwable cause) {
        super(EXCEPTION_MESSAGE_CODE, cause, new Object[]{roleCode});
    }
}
