package com.javamentor.jmp_project.service;

import com.javamentor.jmp_project.dao.UserDao;
import com.javamentor.jmp_project.dao.UserDaoJdbcImpl;
import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.IllegalArgumentException;
import com.javamentor.jmp_project.model.User;

import java.util.List;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class.getName());

    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoJdbcImpl();
    }

    @Override
    public User getUser(Long id) throws DaoException, IllegalArgumentException {
        return userDao.getUser(id);
    }

    @Override
    public User getUserByLogin(String login) throws DaoException, IllegalArgumentException {
        return userDao.getUserByLogin(login);
    }

    @Override
    public List<User> getAllUsers() throws DaoException {
        return userDao.getAllUsers();
    }

    @Override
    public User createUser(User user) throws DaoException, IllegalArgumentException {
        return userDao.createUser(user);
    }

    @Override
    public User updateUser(User user) throws DaoException, IllegalArgumentException {
        return userDao.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) throws DaoException, IllegalArgumentException {
        userDao.deleteUser(id);
    }

    @Override
    public void close() throws DaoException {
        try {
            userDao.close();
        } catch (DaoException e) {
            String msg = "User service close failed";
            LOG.warning(msg + ": " + e.getMessage());
            throw new DaoException(msg + ".", e);
        }
    }

}
