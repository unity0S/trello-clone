package io.molnarsandor.trelloclone.global_exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomGlobalExceptionResponse {
    private final String serverError;


}
