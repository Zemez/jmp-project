package com.javamentor.jmp_project.service;

import com.javamentor.jmp_project.exception.AlreadyExistsException;
import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.IllegalArgumentException;
import com.javamentor.jmp_project.exception.NotFoundException;
import com.javamentor.jmp_project.model.User;

import java.util.List;

public interface UserService extends AutoCloseable {

    User getUser(Long id) throws DaoException, IllegalArgumentException;

    User getUserByLogin(String login) throws DaoException, IllegalArgumentException, NotFoundException;

    List<User> getAllUsers() throws DaoException;

    User createUser(User user) throws DaoException, IllegalArgumentException, AlreadyExistsException;

    User updateUser(User user) throws DaoException, IllegalArgumentException, NotFoundException;

    void deleteUser(Long id) throws DaoException, IllegalArgumentException, NotFoundException;

    @Override
    void close() throws DaoException;

}
