package com.javamentor.jmp_project.dao;

import com.javamentor.jmp_project.exception.AlreadyExistsException;
import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.InvalidArgumentException;
import com.javamentor.jmp_project.exception.NotFoundException;
import com.javamentor.jmp_project.model.User;

import java.util.List;

public interface UserDao {

    User getUser(Long id) throws DaoException, InvalidArgumentException;

    User getUserByLogin(String login) throws DaoException, InvalidArgumentException, NotFoundException;

    List<User> getAllUsers() throws DaoException;

    User createUser(User user) throws DaoException, InvalidArgumentException, AlreadyExistsException;

    User updateUser(User user) throws DaoException, InvalidArgumentException, NotFoundException;

    void deleteUser(Long id) throws DaoException, InvalidArgumentException, NotFoundException;

}
