package io.molnarsandor.trelloclone.globalExceptions;

public class EntityOperationException extends RuntimeException {

    private Integer code = 000;

    public EntityOperationException(String message, int code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
