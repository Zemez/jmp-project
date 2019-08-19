package com.javamentor.jmp_project.service;

import com.javamentor.jmp_project.dao.UserDao;
import com.javamentor.jmp_project.dao.UserDaoFactory;
import com.javamentor.jmp_project.exception.AlreadyExistsException;
import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.InvalidArgumentException;
import com.javamentor.jmp_project.exception.NotFoundException;
import com.javamentor.jmp_project.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {

    private static UserService userService;
    private UserDao userDao;

    private UserServiceImpl() {
        userDao = UserDaoFactory.getUserDao();
    }

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
        return userService;
    }

    @Override
    public User getUser(Long id) throws DaoException, InvalidArgumentException {
        return userDao.getUser(id);
    }

    @Override
    public User getUserByLogin(String login) throws DaoException, InvalidArgumentException, NotFoundException {
        return userDao.getUserByLogin(login);
    }

    @Override
    public List<User> getAllUsers() throws DaoException {
        return userDao.getAllUsers();
    }

    @Override
    public User createUser(User user) throws DaoException, InvalidArgumentException, AlreadyExistsException {
        return userDao.createUser(user);
    }

    @Override
    public User updateUser(User user) throws DaoException, InvalidArgumentException, NotFoundException {
        return userDao.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) throws DaoException, InvalidArgumentException, NotFoundException {
        userDao.deleteUser(id);
    }

}
