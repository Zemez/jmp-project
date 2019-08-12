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
                LOG.warning("Getting user result exception: " + e.getMessage());
                throw new DaoException("Getting user failed.", e);
            }
        } catch (SQLException e) {
            LOG.warning("Getting user statement exception: " + e.getMessage());
            throw new DaoException("Getting user failed.", e);
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
                LOG.warning("Getting users result exception: " + e.getMessage());
                throw new DaoException("Getting users failed.", e);
            }
        } catch (SQLException e) {
            LOG.warning("Getting users statement exception: " + e.getMessage());
            throw new DaoException("Getting users failed.", e);
        }
        return users;
    }

    public void addUser(User user) throws DaoException {
        if (getUserBy("login", user.getLogin()) != null) throw new DaoException("User already exists");

        String sql = "insert into users (login, password, name, email) values (?,?,?,?)";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getEmail());
            LOG.info(statement.toString());

            int result = statement.executeUpdate();
            if (result != 1) throw new DaoException("Adding user failed.");

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new DaoException("Adding user failed, no Id obtained.");
                }
            } catch (SQLException e) {
                LOG.warning("Adding user result exception: " + e.getMessage());
                throw new DaoException("Adding user failed.");
            }
        } catch (SQLException e) {
            LOG.warning("Adding statement exception: " + e.getMessage());
            throw new DaoException("Adding user failed.", e);
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
            LOG.warning("Creating table statement exception: " + e.getMessage());
            throw new DaoException("Creating table failed.", e);
        }
    }

    public void dropTable() throws DaoException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("drop table if exists users");
        } catch (SQLException e) {
            LOG.warning("Dropping table statement exception: " + e.getMessage());
            throw new DaoException("Dropping table failed.", e);
        }
    }

}
