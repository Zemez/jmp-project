package com.javamentor.jmp_project.exception;

public class DataAlreadyExistsException extends Exception {

    public DataAlreadyExistsException(String reason) {
        super(reason);
    }

    public DataAlreadyExistsException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
