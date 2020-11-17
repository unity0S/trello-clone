package io.molnarsandor.trelloclone.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CustomInternalServerErrorExceptionResponse {

    private String serverError;
}
