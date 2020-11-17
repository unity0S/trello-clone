package io.molnarsandor.trelloclone.collaborator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CollaboratorAlreadyAssignedException extends RuntimeException {

    public CollaboratorAlreadyAssignedException(String message) {
        super(message);
    }
}
