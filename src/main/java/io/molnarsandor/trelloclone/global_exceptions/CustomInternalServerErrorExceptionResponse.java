package io.molnarsandor.trelloclone.global_exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomInternalServerErrorExceptionResponse {

    private String serverError;

    public CustomInternalServerErrorExceptionResponse() {
        this.serverError = "Internal Server Error";
    }
}
