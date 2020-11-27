package io.molnarsandor.trelloclone.user.exceptions;

import io.molnarsandor.trelloclone.globalExceptions.EntityOperationException;

public class ActivationKeyNotFoundException extends EntityOperationException {

    public ActivationKeyNotFoundException(String message) {
        super(message, 404);
    }
}
