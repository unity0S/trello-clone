package io.molnarsandor.trelloclone.project.exceptions;

import io.molnarsandor.trelloclone.globalExceptions.EntityOperationException;

public class ProjectIdException extends EntityOperationException {

    public ProjectIdException(String message) {
        super(message, 409);
    }
}
