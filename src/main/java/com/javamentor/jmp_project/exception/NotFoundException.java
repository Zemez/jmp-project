package com.javamentor.jmp_project.exception;

public class NotFoundException extends Exception {

    public NotFoundException(String reason) {
        super(reason);
    }

    public NotFoundException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
