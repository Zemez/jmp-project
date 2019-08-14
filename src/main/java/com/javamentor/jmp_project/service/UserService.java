package com.javamentor.jmp_project.service;

import com.javamentor.jmp_project.config.MySqlConfig;
import com.javamentor.jmp_project.dao.UserDao;
import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.DbException;
import com.javamentor.jmp_project.model.User;

import java.util.List;

public class UserService implements AutoCloseable {

    private MySqlConfig mySqlConfig;
    private UserDao userDao;

    public UserService() {
        try {
            mySqlConfig = new MySqlConfig();
            userDao = new UserDao(mySqlConfig.getConnection());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public User getUser(Long id) throws DaoException {
        return userDao.getUser(id);
    }

    public User getUserByLogin(String login) throws DaoException {
        return userDao.getUserByLogin(login);
    }

    public List<User> getAllUsers() throws DaoException {
        return userDao.getAllUsers();
    }

    public User createUser(User user) throws DaoException {
        return userDao.createUser(user);
    }

    public User updateUser(User user) throws DaoException {
        return userDao.updateUser(user);
    }

    public void deleteUser(Long id) throws DaoException {
        userDao.deleteUser(id);
    }

    @Override
    public void close() {
        try {
            mySqlConfig.close();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
