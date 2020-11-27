package io.molnarsandor.trelloclone.project.exceptions;

import io.molnarsandor.trelloclone.globalExceptions.EntityOperationException;

public class ProjectNotFoundException extends EntityOperationException {

    public ProjectNotFoundException(String message) {
        super(message, 404);
    }
}
