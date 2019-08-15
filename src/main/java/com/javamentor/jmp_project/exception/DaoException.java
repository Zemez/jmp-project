package com.javamentor.jmp_project.exception;

public class DaoException extends Exception {

    public DaoException(String reason) {
        super(reason);
    }

    public DaoException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
