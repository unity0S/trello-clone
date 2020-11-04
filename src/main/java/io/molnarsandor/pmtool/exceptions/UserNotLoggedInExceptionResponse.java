package io.molnarsandor.pmtool.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserNotLoggedInExceptionResponse {

    private String userNotLoggedIn;
}
