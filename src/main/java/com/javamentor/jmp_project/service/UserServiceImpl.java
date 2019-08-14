package com.javamentor.jmp_project.service;

import com.javamentor.jmp_project.config.MySqlConfig;
import com.javamentor.jmp_project.dao.UserDaoImpl;
import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.DbException;
import com.javamentor.jmp_project.model.User;

import java.util.List;

public class UserServiceImpl implements UserService, AutoCloseable {

    private MySqlConfig mySqlConfig;
    private UserDaoImpl userDao;

    public UserServiceImpl() {
        try {
            mySqlConfig = new MySqlConfig();
            userDao = new UserDaoImpl(mySqlConfig.getConnection());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUser(Long id) throws DaoException {
        return userDao.getUser(id);
    }

    @Override
    public User getUserByLogin(String login) throws DaoException {
        return userDao.getUserByLogin(login);
    }

    @Override
    public List<User> getAllUsers() throws DaoException {
        return userDao.getAllUsers();
    }

    @Override
    public User createUser(User user) throws DaoException {
        return userDao.createUser(user);
    }

    @Override
    public User updateUser(User user) throws DaoException {
        return userDao.updateUser(user);
    }

    @Override
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
