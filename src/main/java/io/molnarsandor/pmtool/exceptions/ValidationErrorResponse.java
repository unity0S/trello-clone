package io.molnarsandor.pmtool.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class ValidationErrorResponse {

    private Map<String,String> validationError;
}
