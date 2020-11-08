package io.molnarsandor.pmtool.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Log log = LogFactory.getLog(this.getClass());

    @ExceptionHandler
    public final ResponseEntity<Object> handleProjectIdException(ProjectIdException ex, WebRequest request) {
        ProjectIdExceptionResponse exceptionResponse = new ProjectIdExceptionResponse(ex.getMessage());
        log.error(exceptionResponse.getProjectIdentifier());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException ex, WebRequest request) {
        ProjectNotFoundExceptionResponse exceptionResponse = new ProjectNotFoundExceptionResponse(ex.getMessage());
        log.error(exceptionResponse.getProjectNotFound());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex, WebRequest request) {
        UsernameAlreadyExistsResponse exceptionResponse = new UsernameAlreadyExistsResponse(ex.getMessage());
        log.error(exceptionResponse.getUsername());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleUserNotLoggedInException(UserNotLoggedInException ex, WebRequest request) {
        UserNotLoggedInExceptionResponse exceptionResponse = new UserNotLoggedInExceptionResponse(ex.getMessage());
        log.error((exceptionResponse.getUserNotLoggedIn()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleCollaboratorAlreadyAssignedException(CollaboratorAlreadyAssignedException ex, WebRequest request) {
        CollaboratorAlreadyAssignedExceptionResponse exceptionResponse = new CollaboratorAlreadyAssignedExceptionResponse(ex.getMessage());
        log.error(exceptionResponse.getCollaborator());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleValidationErrorException(ValidationErrorException ex, WebRequest request) {
        ValidationErrorResponse exceptionResponse = new ValidationErrorResponse(ex.getMsg());
        log.error(exceptionResponse.getValidationError().toString());
        return new ResponseEntity<>(exceptionResponse.getValidationError(), HttpStatus.BAD_REQUEST);
    }
}
