package com.javamentor.jmp_project.exception;

public class AlreadyExistsException extends Exception {

    public AlreadyExistsException(String reason) {
        super(reason);
    }

    public AlreadyExistsException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
