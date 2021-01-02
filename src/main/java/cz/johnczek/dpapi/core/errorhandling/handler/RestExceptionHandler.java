package cz.johnczek.dpapi.core.errorhandling.handler;

import cz.johnczek.dpapi.core.errorhandling.exception.*;
import cz.johnczek.dpapi.core.rest.response.BaseRestResponse;
import cz.johnczek.dpapi.core.rest.response.RestMessage;
import cz.johnczek.dpapi.core.rest.response.RestMessageTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@Order(1)
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    private static final String DEFAULT_MESSAGE = "V aplikaci nastala chyba";

    private final MessageSource messageSource;


    @ExceptionHandler(BaseNotFoundRestException.class)
    public ResponseEntity<BaseRestResponse> handleNotFound(BaseRestException exception) {
        return prepareResponseEntity(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BaseBadRequestRestException.class)
    public ResponseEntity<BaseRestResponse> handleBadRequest(BaseRestException exception) {
        return prepareResponseEntity(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaseForbiddenRestException.class)
    public ResponseEntity<BaseRestResponse> handleForbidden(BaseRestException exception) {
        return prepareResponseEntity(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BaseMethodNotAllowedRestException.class)
    public ResponseEntity<BaseRestResponse> handleMethodNotAllowed(BaseRestException exception) {
        return prepareResponseEntity(exception, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(BaseRestException.class)
    public ResponseEntity<String> handleDefault(Throwable t) {
        return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Generates response entity from exception and given status.
     *
     * @param exception exception to be parsed into response
     * @param status    status of response
     * @return response entity containing list of error messages and given status code
     */
    private ResponseEntity<BaseRestResponse> prepareResponseEntity(BaseRestException exception, HttpStatus status) {
        BaseRestResponse response = new BaseRestResponse();
        response.addMessage(getMessage(exception.getMessage(), exception.getArgs()));

        return new ResponseEntity<>(response, status);
    }

    /**
     * Method generates RestMessage of type {@code RestMessageType.ERROR} for exception.
     *
     * @param messageCode messageCode for which will be found properties message
     * @param args        if properties message has arguments, these arguments will be used to fill them
     * @return formatted error message
     */
    private RestMessage getMessage(String messageCode, Object[] args) {
        return RestMessage.builder()
                .value(messageSource.getMessage(messageCode, args, DEFAULT_MESSAGE, Locale.getDefault()))
                .messageType(RestMessageTypeEnum.ERROR)
                .build();
    }
}
