package io.molnarsandor.trelloclone.globalExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomGlobalExceptionResponse {
    private final String serverError;


}
