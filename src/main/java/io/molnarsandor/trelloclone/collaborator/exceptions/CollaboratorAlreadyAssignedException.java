package io.molnarsandor.trelloclone.collaborator.exceptions;

import io.molnarsandor.trelloclone.globalExceptions.EntityOperationException;

public class CollaboratorAlreadyAssignedException extends EntityOperationException {

    public CollaboratorAlreadyAssignedException(String message) {
        super(message, 409);
    }
}
