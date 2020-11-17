package io.molnarsandor.trelloclone.user.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UsernameAlreadyExistsExceptionResponse {

    private String username;
}
