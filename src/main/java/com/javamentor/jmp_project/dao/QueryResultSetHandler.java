package com.javamentor.jmp_project.dao;

import com.javamentor.jmp_project.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface QueryResultSetHandler<T> {
    T handle(ResultSet resultSet) throws SQLException, DaoException;
}
