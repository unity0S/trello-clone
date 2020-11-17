package io.molnarsandor.trelloclone.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class ValidationErrorExceptionResponse {

    private Map<String,String> validationError;
}
