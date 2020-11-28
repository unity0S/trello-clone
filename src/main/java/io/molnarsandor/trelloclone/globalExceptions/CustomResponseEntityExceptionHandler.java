package io.molnarsandor.trelloclone.globalExceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Log log = LogFactory.getLog(this.getClass());
    private static final String ERROR_MESSAGE = "\nUri: %s \nUser: %s \nMessage: %s";

    @ExceptionHandler({
        EntityOperationException.class
    })
    public final ResponseEntity<CustomGeneralExceptionResponse> handleBusinessExceptions(EntityOperationException ex, HttpServletRequest req) {
        CustomGeneralExceptionResponse exceptionResponse = new CustomGeneralExceptionResponse(ex.getCode(), "projectId", ex.getMessage());
        log.error(String.format(ERROR_MESSAGE, req.getRequestURI(), req.getRemoteUser(), ex.getMessage()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public final ResponseEntity<CustomGeneralExceptionResponse> handleValidationErrorException(ValidationErrorException ex, HttpServletRequest req) {
        CustomGeneralExceptionResponse exceptionResponse = new CustomGeneralExceptionResponse(ValidationErrorException.CODE, ex.getError());
        log.error(String.format(ERROR_MESSAGE, req.getRequestURI(), req.getRemoteUser(), ex.getMessage()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public final ResponseEntity<CustomGeneralExceptionResponse> handleGlobalException(HttpServletRequest req, Exception ex) {
        CustomGeneralExceptionResponse exceptionResponse = new CustomGeneralExceptionResponse(500, "key", ex.getMessage());
        log.error(String.format(ERROR_MESSAGE, req.getRequestURI(), req.getRemoteUser(), ex.getMessage()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
