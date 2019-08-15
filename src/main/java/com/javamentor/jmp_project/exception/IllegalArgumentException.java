package com.javamentor.jmp_project.exception;

public class IllegalArgumentException extends Exception {

    public IllegalArgumentException(String reason) {
        super(reason);
    }

    public IllegalArgumentException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
