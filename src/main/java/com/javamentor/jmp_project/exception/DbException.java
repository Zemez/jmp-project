package com.javamentor.jmp_project.exception;

import java.sql.SQLException;

public class DbException extends SQLException {

    public DbException(String reason) {
        super(reason);
    }

    public DbException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
