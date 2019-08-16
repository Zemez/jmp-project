package com.javamentor.jmp_project.exception;

public class DataNotFoundException extends Exception {

    public DataNotFoundException(String reason) {
        super(reason);
    }

    public DataNotFoundException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
