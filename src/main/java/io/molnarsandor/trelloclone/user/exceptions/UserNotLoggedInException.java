package io.molnarsandor.trelloclone.user.exceptions;

import io.molnarsandor.trelloclone.globalExceptions.EntityOperationException;

public class UserNotLoggedInException extends EntityOperationException {

    public UserNotLoggedInException(String message) {
        super(message, 401);
    }
}
