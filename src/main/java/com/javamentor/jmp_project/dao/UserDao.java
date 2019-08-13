package com.javamentor.jmp_project.dao;

import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDao {

    private static final Logger LOG = Logger.getLogger(UserDao.class.getName());
    private Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public User getUser(Long id) throws DaoException {
        if (id <= 0) return null;

        return getUserBy("id", id);
    }

    public User getUserBy(String field, Object value) throws DaoException {
        if (String.valueOf(field).isEmpty() || String.valueOf(value).isEmpty()) return null;

        String sql = "select * from users where " + field + "=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, value);
            LOG.info(statement.toString());

            try (ResultSet result = statement.executeQuery()) {
                if (!result.first()) return null;

                Long id = result.getLong("id");
                String login = result.getString("login");
                String password = result.getString("password");
                String name = result.getString("name");
                String email = result.getString("email");

                return new User(id, login, password, name, email);
            } catch (SQLException e) {
                LOG.warning("User get result exception: " + e.getMessage());
                throw new DaoException("User get failed.", e);
            }
        } catch (SQLException e) {
            LOG.warning("User get statement exception: " + e.getMessage());
            throw new DaoException("User get failed.", e);
        }
    }

    public List<User> getAllUsers() throws DaoException {
        List<User> users = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            statement.executeQuery("select * from users");
            LOG.info(statement.toString());

            try (ResultSet resultSet = statement.getResultSet()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String login = resultSet.getString("login");
                    String password = resultSet.getString("password");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");

                    users.add(new User(id, login, password, name, email));
                }
            } catch (SQLException e) {
                LOG.warning("Users get result exception: " + e.getMessage());
                throw new DaoException("Users get failed.", e);
            }
        } catch (SQLException e) {
            LOG.warning("Users get statement exception: " + e.getMessage());
            throw new DaoException("Users get failed.", e);
        }
        return users;
    }

    public User addUser(String login, String password, String name, String email) throws DaoException {
        if (getUserBy("login", login) != null) throw new DaoException("User already exists.");

        String sql = "insert into users (login, password, name, email) values (?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, name);
            statement.setString(4, email);
            LOG.info(statement.toString());

            int result = statement.executeUpdate();
            if (result != 1) throw new DaoException("User add failed.");

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    return getUser(generatedKeys.getLong(1));
                } else {
                    throw new DaoException("User add failed, no Id obtained.");
                }
            } catch (SQLException e) {
                LOG.warning("User add result exception: " + e.getMessage());
                throw new DaoException("User add failed.");
            }
        } catch (SQLException e) {
            LOG.warning("User add statement exception: " + e.getMessage());
            throw new DaoException("User add failed.", e);
        }
    }

    public User updateUser(Long id, String login, String password, String name, String email) throws DaoException {
        User user = getUser(id);
        if (user == null) throw new DaoException("User not found.");
        if (!user.getLogin().equals(login)) throw new DaoException("Couldn't change login.");

        StringBuilder sql = new StringBuilder("update users set");
        List<Object> list = new ArrayList<>();

        if (!user.getPassword().equals(password)) {
            sql.append(" password=?");
            list.add(password);
        }
        if (!user.getName().equals(name)) {
            sql.append(" name=?");
            list.add(name);
        }
        if (!user.getEmail().equals(email)) {
            sql.append(" email=?");
            list.add(email);
        }

        if (list.size() < 1) throw new DaoException("Nothing to update.");

        sql.append(" where id=?");
        list.add(id);

        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < list.size(); i++) {
                statement.setObject(i + 1, list.get(i));
            }
            LOG.info(statement.toString());

            int rows = statement.executeUpdate();
            if (rows != 1) throw new DaoException("User update failed.");
            user = getUser(id);
        } catch (SQLException e) {
            LOG.warning("User update statement exception: " + e.getMessage());
            throw new DaoException("User update failed.", e);
        }
        return user;
    }

    public void deleteUser(Long id) throws DaoException {
        String sql = "delete from users where id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            LOG.info(statement.toString());

            int result = statement.executeUpdate();
            if (result != 1) throw new DaoException("User delete failed.");
        } catch (SQLException e) {
            LOG.warning("User delete statement exception: " + e.getMessage());
            throw new DaoException("User delete failed.", e);
        }
    }

    public void createTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("create table if not exists users (" +
                    "id bigint auto_increment primary key," +
                    "login varchar(255) not null unique," +
                    "password varchar(255) not null," +
                    "name varchar(255) null," +
                    "email varchar(255) null" +
                    ") comment 'User table'");
        } catch (SQLException e) {
            LOG.warning("Table create statement exception: " + e.getMessage());
            throw new DaoException("Table create failed.", e);
        }
    }

    @SuppressWarnings("unused")
    public void dropTable() throws DaoException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("drop table if exists users");
        } catch (SQLException e) {
            LOG.warning("Table drop statement exception: " + e.getMessage());
            throw new DaoException("Table drop failed.", e);
        }
    }

}
