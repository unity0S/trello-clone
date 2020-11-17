package io.molnarsandor.trelloclone.global_exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class ValidationErrorException extends RuntimeException {

    private Map<String, String> msg;

    public ValidationErrorException(Map<String, String> message) {
        this.msg = message;
    }
}
