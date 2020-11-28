package io.molnarsandor.trelloclone.globalExceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class ValidationErrorException extends RuntimeException {

    public static final Integer CODE = 400;
    private final Map<String, String> error;

    public ValidationErrorException(Map<String, String> message) {
        this.error = message;
    }
}
