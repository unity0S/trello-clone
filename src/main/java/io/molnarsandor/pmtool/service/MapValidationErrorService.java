package io.molnarsandor.pmtool.service;

import io.molnarsandor.pmtool.exceptions.ValidationErrorException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class MapValidationErrorService {

    public void MapValidationService(BindingResult result) {

        if(result.hasErrors()) {
            Map<String, String> errorMap =
                    result.getFieldErrors()
                            .stream()
                            .collect(toMap(FieldError::getField, FieldError::getDefaultMessage, (e1, e2) -> e1));

            throw new ValidationErrorException(errorMap);
        }
    }
}
