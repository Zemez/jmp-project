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

    private UserDao getUserDao() throws DbException {
        return new UserDao(dbService.getConnection());
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

    public User addUser(String login, String password, String name, String email) throws DbException, DaoException {
        return getUserDao().addUser(login, password, name, email);
    }

    public User updateUser(Long id, String login, String password, String name, String email) throws DbException, DaoException {
        return getUserDao().updateUser(id, login, password, name, email);
    }
    public void deleteUser(Long id) throws DbException, DaoException {
        getUserDao().deleteUser(id);
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
