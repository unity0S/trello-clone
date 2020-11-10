package io.molnarsandor.pmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProjectIdException extends RuntimeException {

    public ProjectIdException(String message) {
        super(message);
    }
}
