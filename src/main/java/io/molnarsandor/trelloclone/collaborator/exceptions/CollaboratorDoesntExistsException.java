package io.molnarsandor.trelloclone.collaborator.exceptions;

import io.molnarsandor.trelloclone.globalExceptions.EntityOperationException;

public class CollaboratorDoesntExistsException extends EntityOperationException {
    public CollaboratorDoesntExistsException(String message) {
        super(message, 500);
    }
}
