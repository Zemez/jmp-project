package com.javamentor.jmp_project.dao;

import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.model.User;

import java.util.List;

public interface UserDao {

    User getUser(Long id) throws DaoException;

    User getUserByLogin(String login) throws DaoException;

    List<User> getAllUsers() throws DaoException;

    User createUser(User user) throws DaoException;

    User updateUser(User user) throws DaoException;

    void deleteUser(Long id) throws DaoException;

    void createTable() throws DaoException;

    void dropTable() throws DaoException;

}
