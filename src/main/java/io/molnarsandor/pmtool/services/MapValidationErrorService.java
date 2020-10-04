package io.molnarsandor.pmtool.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class MapValidationErrorService {

    public ResponseEntity<?> MapValidationService(BindingResult result) {

        if(result.hasErrors()) {
            Map<String, String> errorMap =
                    result.getFieldErrors()
                            .stream()
                            .collect(toMap(FieldError::getField, FieldError::getDefaultMessage, (e1, e2) -> e1));

            return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
        }

        return null;

    }
}
