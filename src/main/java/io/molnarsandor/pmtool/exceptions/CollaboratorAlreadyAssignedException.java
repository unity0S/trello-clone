package io.molnarsandor.pmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CollaboratorAlreadyAssignedException extends RuntimeException{

    public CollaboratorAlreadyAssignedException(String message) {
        super(message);
    }
}
