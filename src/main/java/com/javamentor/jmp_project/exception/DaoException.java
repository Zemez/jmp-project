package com.javamentor.jmp_project.exception;

import java.sql.SQLException;

public class DaoException extends SQLException {

    public DaoException(String reason) {
        super(reason);
    }

    public DaoException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
