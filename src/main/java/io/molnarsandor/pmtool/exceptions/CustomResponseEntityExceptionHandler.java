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

import java.util.Map;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Log log = LogFactory.getLog(this.getClass());

    @ExceptionHandler
    public final ResponseEntity<ProjectIdExceptionResponse> handleProjectIdException(ProjectIdException ex, WebRequest request) {
        ProjectIdExceptionResponse exceptionResponse = new ProjectIdExceptionResponse(ex.getMessage());
        log.error(exceptionResponse.getProjectIdentifier());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<ProjectNotFoundExceptionResponse> handleProjectNotFoundException(ProjectNotFoundException ex, WebRequest request) {
        ProjectNotFoundExceptionResponse exceptionResponse = new ProjectNotFoundExceptionResponse(ex.getMessage());
        log.error(exceptionResponse.getProjectNotFound());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<UsernameAlreadyExistsResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex, WebRequest request) {
        UsernameAlreadyExistsResponse exceptionResponse = new UsernameAlreadyExistsResponse(ex.getMessage());
        log.error(exceptionResponse.getUsername());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<UserNotLoggedInExceptionResponse> handleUserNotLoggedInException(UserNotLoggedInException ex, WebRequest request) {
        UserNotLoggedInExceptionResponse exceptionResponse = new UserNotLoggedInExceptionResponse(ex.getMessage());
        log.error((exceptionResponse.getUserNotLoggedIn()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<CollaboratorAlreadyAssignedExceptionResponse> handleCollaboratorAlreadyAssignedException(CollaboratorAlreadyAssignedException ex, WebRequest request) {
        CollaboratorAlreadyAssignedExceptionResponse exceptionResponse = new CollaboratorAlreadyAssignedExceptionResponse(ex.getMessage());
        log.error(exceptionResponse.getCollaborator());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Map<String, String>> handleValidationErrorException(ValidationErrorException ex, WebRequest request) {
        ValidationErrorResponse exceptionResponse = new ValidationErrorResponse(ex.getMsg());
        log.error(exceptionResponse.getValidationError().toString());
        return new ResponseEntity<>(exceptionResponse.getValidationError(), HttpStatus.BAD_REQUEST);
    }
}
