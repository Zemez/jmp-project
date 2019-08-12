package com.javamentor.jmp_project.service;

import com.javamentor.jmp_project.dao.UserDao;
import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.DbException;
import com.javamentor.jmp_project.model.User;

import java.util.List;

public class UserService implements AutoCloseable {

    private DbService dbService;

    public UserService() {
        try {
            dbService = new DbService();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public User getUser(Long id) throws DbException, DaoException {
        return getUserDao().getUser(id);
    }

    public User getUserBy(String field, Object value) throws DbException, DaoException {
        return getUserDao().getUserBy(field, value);
    }

    public List<User> getAllUsers() throws DbException, DaoException {
        return getUserDao().getAllUsers();
    }

    private UserDao getUserDao() throws DbException, DaoException {
        return new UserDao(dbService.getConnection());
    }

    @Override
    public void close() {
        try {
            dbService.close();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
