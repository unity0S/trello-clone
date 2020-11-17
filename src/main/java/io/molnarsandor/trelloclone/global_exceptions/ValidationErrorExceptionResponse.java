package io.molnarsandor.trelloclone.global_exceptions;

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
