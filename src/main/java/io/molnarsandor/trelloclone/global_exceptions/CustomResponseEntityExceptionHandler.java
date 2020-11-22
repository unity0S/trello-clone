package io.molnarsandor.trelloclone.global_exceptions;

import io.molnarsandor.trelloclone.collaborator.exceptions.CollaboratorAlreadyAssignedException;
import io.molnarsandor.trelloclone.collaborator.exceptions.CollaboratorAlreadyAssignedExceptionResponse;
import io.molnarsandor.trelloclone.project.exceptions.ProjectIdException;
import io.molnarsandor.trelloclone.project.exceptions.ProjectIdExceptionResponse;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundException;
import io.molnarsandor.trelloclone.project.exceptions.ProjectNotFoundExceptionResponse;
import io.molnarsandor.trelloclone.user.exceptions.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Log log = LogFactory.getLog(this.getClass());

    @ExceptionHandler
    public final ResponseEntity<ProjectIdExceptionResponse> handleProjectIdException(ProjectIdException ex, WebRequest request) {
        ProjectIdExceptionResponse exceptionResponse = new ProjectIdExceptionResponse(ex.getMessage());
        log.error(exceptionResponse.getProjectIdentifier());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public final ResponseEntity<ProjectNotFoundExceptionResponse> handleProjectNotFoundException(ProjectNotFoundException ex, WebRequest request) {
        ProjectNotFoundExceptionResponse exceptionResponse = new ProjectNotFoundExceptionResponse(ex.getMessage());
        log.error(exceptionResponse.getProjectNotFound());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public final ResponseEntity<UsernameAlreadyExistsExceptionResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex, WebRequest request) {
        UsernameAlreadyExistsExceptionResponse exceptionResponse = new UsernameAlreadyExistsExceptionResponse(ex.getMessage());
        log.error(exceptionResponse.getUsername());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public final ResponseEntity<UserNotLoggedInExceptionResponse> handleUserNotLoggedInException(UserNotLoggedInException ex, WebRequest request) {
        UserNotLoggedInExceptionResponse exceptionResponse = new UserNotLoggedInExceptionResponse(ex.getMessage());
        log.error((exceptionResponse.getUserNotLoggedIn()));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public final ResponseEntity<CollaboratorAlreadyAssignedExceptionResponse> handleCollaboratorAlreadyAssignedException(CollaboratorAlreadyAssignedException ex, WebRequest request) {
        CollaboratorAlreadyAssignedExceptionResponse exceptionResponse = new CollaboratorAlreadyAssignedExceptionResponse(ex.getMessage());
        log.error(exceptionResponse.getCollaborator());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public final ResponseEntity<Map<String, String>> handleValidationErrorException(ValidationErrorException ex, WebRequest request) {
        ValidationErrorExceptionResponse exceptionResponse = new ValidationErrorExceptionResponse(ex.getMsg());
        log.error(exceptionResponse.getValidationError().toString());
        return new ResponseEntity<>(exceptionResponse.getValidationError(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<ActivationKeyNotFoundExceptionResponse> handleAvtivationKeyNotFoundException(ActivationKeyNotFoundException ex, WebRequest request) {
        ActivationKeyNotFoundExceptionResponse exceptionResponse = new ActivationKeyNotFoundExceptionResponse(ex.getMessage());
        log.error(ex);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    public final ResponseEntity<CustomGlobalExceptionResponse> handleGlobalException(HttpServletRequest req, Exception e) {
        log.error("Request: " + req.getRequestURL() + " raised " + e);
        return new ResponseEntity<>(new CustomGlobalExceptionResponse("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
