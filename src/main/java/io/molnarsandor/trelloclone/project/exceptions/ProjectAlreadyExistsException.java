package io.molnarsandor.trelloclone.project.exceptions;

import io.molnarsandor.trelloclone.globalExceptions.EntityOperationException;

public class ProjectAlreadyExistsException extends EntityOperationException {

    public ProjectAlreadyExistsException(String message) {
        super(message, 409);
    }
}
