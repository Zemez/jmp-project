package com.javamentor.jmp_project.exception;

public class InvalidArgumentException extends Exception {

    public InvalidArgumentException(String reason) {
        super(reason);
    }

    public InvalidArgumentException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
