package io.molnarsandor.trelloclone.globalExceptions;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomGeneralExceptionResponse {

    private final Integer code;
    private final Map<String, String> error;

    public CustomGeneralExceptionResponse(Integer code, Map<String, String> errorMap) {
        this.code = code;
        this.error = errorMap;
    }

    public CustomGeneralExceptionResponse(Integer code, String key, String value) {
        this.code = code;
        this.error = new HashMap<>();
        this.error.put(key, value);
    }
}
