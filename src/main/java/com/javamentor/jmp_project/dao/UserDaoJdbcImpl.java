package com.javamentor.jmp_project.dao;

import com.javamentor.jmp_project.dao.handler.QueryResultSetHandler;
import com.javamentor.jmp_project.dao.handler.UpdateResultSetHandler;
import com.javamentor.jmp_project.exception.AlreadyExistsException;
import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.InvalidArgumentException;
import com.javamentor.jmp_project.exception.NotFoundException;
import com.javamentor.jmp_project.model.User;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class UserDaoJdbcImpl implements UserDao, AutoCloseable {

    private static final Logger LOG = Logger.getLogger(UserDaoJdbcImpl.class.getName());
    private Connection connection;

    public UserDaoJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User getUser(Long id) throws DaoException, InvalidArgumentException {
        if (id == null || id < 1) throw new InvalidArgumentException("Invalid user id.");

        return getUserBy("id", id);
    }

    @Override
    public User getUserByLogin(String login) throws DaoException, InvalidArgumentException {
        if (StringUtils.isBlank(login)) throw new InvalidArgumentException("Invalid user login.");

        return getUserBy("login", login);
    }

    private User getUserBy(String field, Object value) throws DaoException {
        return execPreparedQuery("select * from users where " + field + "=? limit 1", Collections.singletonList(value),
                rs -> {
                    if (rs.first()) return new User(
                            rs.getLong("id"),
                            rs.getString("login"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("name"),
                            rs.getString("email")
                    );
                    return null;
                });
    }

    @Override
    public List<User> getAllUsers() throws DaoException {
        return execPreparedQuery("select * from users", Collections.emptyList(),
                rs -> {
                    List<User> users = new ArrayList<>();
                    while (rs.next()) {
                        users.add(new User(
                                rs.getLong("id"),
                                rs.getString("login"),
                                rs.getString("password"),
                                rs.getString("role"),
                                rs.getString("name"),
                                rs.getString("email")
                        ));
                    }
                    return users;
                });
    }

    @Override
    public User createUser(User user) throws DaoException, InvalidArgumentException, AlreadyExistsException {
        if (user == null) throw new InvalidArgumentException("Invalid null user.");
        if (getUserBy("login", user.getLogin()) != null) throw new AlreadyExistsException("User already exists.");

        Long id = execPreparedUpdate("insert into users (login,password,role,name,email) values (?,?,?,?,?)",
                Arrays.asList(user.getLogin(), user.getPassword(), user.getRole(), user.getName(), user.getEmail()),
                (rows, keys) -> {
                    if (rows < 1) throw new DaoException("User add failed.");
                    if (rows > 1) throw new DaoException("Something going wrong: more than 1 rows created!");
                    return keys.first() ? keys.getLong(1) : null;
                });

        return getUser(id);
    }

    @Override
    public User updateUser(User user) throws DaoException, InvalidArgumentException, NotFoundException {
        if (user == null) throw new InvalidArgumentException("Invalid null user.");

        Long id = user.getId();

        if (id == null || id < 1) throw new InvalidArgumentException("Invalid user id.");

        User userOld = getUser(id);

        if (userOld == null) throw new NotFoundException("User not found.");
        if (!userOld.getLogin().equals(user.getLogin())) throw new InvalidArgumentException("Couldn't change login.");

        StringJoiner joiner = new StringJoiner(",");
        List<Object> params = new ArrayList<>();

        // could be shot my leg
        for (Field field : User.class.getDeclaredFields()) {
            String fieldName = field.getName();
            if (!Arrays.asList("id", "login").contains(fieldName)) {
                field.setAccessible(true);
                try {
                    if (!field.get(userOld).equals(field.get(user))) {
                        joiner.add(fieldName + "=?");
                        params.add(field.get(user));
                    }
                } catch (IllegalAccessException e) {
                    LOG.warning("User field access exception: " + e.getMessage());
                    throw new DaoException("User fields access failed.");
                }
            }
        }

        if (params.size() < 1) throw new DaoException("Nothing to update.");

        params.add(user.getId());
        int rows = execPreparedUpdate("update users set " + joiner + " where id=? limit 1", params);

        if (rows < 1) throw new DaoException("User update failed.");
        if (rows > 1) throw new DaoException("Something going wrong: more than 1 rows updated!");

        return getUser(id);
    }

    @Override
    public void deleteUser(Long id) throws DaoException, InvalidArgumentException, NotFoundException {
        if (getUser(id) == null) throw new NotFoundException("User not found.");

        int rows = execPreparedUpdate("delete from users where id=? limit 1", Collections.singletonList(id));

        if (rows < 1) throw new DaoException("User delete failed.");
        if (rows > 1) throw new DaoException("Something going wrong! More than 1 rows deleted!");
    }

    // Executors
    private <T> T execPreparedQuery(String sql, List<Object> params, QueryResultSetHandler<T> handler) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            LOG.info("statement: " + statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                return handler.handle(resultSet);
            } catch (SQLException e) {
                LOG.warning("User query result exception: " + e.getMessage());
                throw new DaoException("User query result failed", e);
            }
        } catch (SQLException e) {
            LOG.warning("User query statement exception: " + e.getMessage());
            throw new DaoException("User query statement failed.", e);
        }
    }

    private int execPreparedUpdate(String sql, List<Object> params) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            LOG.info("statement: " + statement);
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOG.warning("User update statement exception: " + e.getMessage());
            throw new DaoException("User update statement failed.", e);
        }
    }

    private <T> T execPreparedUpdate(String sql, List<Object> params, UpdateResultSetHandler<T> handler) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            LOG.info("statement: " + statement);
            int rows = statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                return handler.handle(rows, generatedKeys);
            } catch (SQLException e) {
                LOG.warning("User update result exception: " + e.getMessage());
                throw new DaoException("User update result failed", e);
            }
        } catch (SQLException e) {
            LOG.warning("User update statement exception: " + e.getMessage());
            throw new DaoException("User update statement failed.", e);
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
