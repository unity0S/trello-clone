package io.molnarsandor.trelloclone.user.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ActivationKeyNotFoundExceptionResponse {

    private String activationKey;
}
