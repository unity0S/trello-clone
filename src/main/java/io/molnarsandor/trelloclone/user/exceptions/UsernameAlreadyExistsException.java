package io.molnarsandor.trelloclone.user.exceptions;

import io.molnarsandor.trelloclone.globalExceptions.EntityOperationException;

public class UsernameAlreadyExistsException extends EntityOperationException {

    public UsernameAlreadyExistsException(String message) {
        super(message, 409);
    }
}
