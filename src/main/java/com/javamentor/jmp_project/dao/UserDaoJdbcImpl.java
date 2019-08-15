package com.javamentor.jmp_project.dao;

import com.javamentor.jmp_project.config.JdbcConfig;
import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.IllegalArgumentException;
import com.javamentor.jmp_project.model.User;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJdbcImpl implements UserDao {

    private static final Logger LOG = Logger.getLogger(UserDaoJdbcImpl.class.getName());
    private Connection connection;

    public UserDaoJdbcImpl() {
        connection = JdbcConfig.getConnection();
    }

    @Override
    public User getUser(Long id) throws DaoException, IllegalArgumentException {
        if (id == null || id < 1) throw new IllegalArgumentException("Invalid id.");

        return getUserBy("id", id);
    }

    @Override
    public User getUserByLogin(String login) throws DaoException, IllegalArgumentException {
        if (StringUtils.isBlank(login)) throw new IllegalArgumentException("Invalid login.");

        return getUserBy("login", login);
    }

    private User getUserBy(String field, Object value) throws DaoException {
        if (StringUtils.isBlank(field) || String.valueOf(value).isBlank()) throw new DaoException("Invalid data.");

        String sql = "select * from users where " + field + "=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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

    @Override
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

    @Override
    public User createUser(User user) throws DaoException, IllegalArgumentException {
        if (getUserBy("login", user.getLogin()) != null) throw new DaoException("User already exists.");

        String sql = "insert into users (login, password, name, email) values (?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getEmail());
            LOG.info(statement.toString());

            int result = statement.executeUpdate();
            if (result != 1) throw new DaoException("User add failed.");

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.first()) {
                    return getUser(generatedKeys.getLong(1));
                } else {
                    throw new DaoException("User add failed, no id obtained.");
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

    @Override
    public User updateUser(User user) throws DaoException, IllegalArgumentException {
        if (user == null) throw new IllegalArgumentException("Invalid user.");

        Long id = user.getId();

        if (id == null || id < 1) throw new IllegalArgumentException("Invalid id.");

        User userOld = getUser(id);

        if (userOld == null) throw new DaoException("User not found.");
        if (!userOld.getLogin().equals(user.getLogin())) throw new IllegalArgumentException("Couldn't change login.");

        StringBuilder sql = new StringBuilder("update users set");
        List<Object> list = new ArrayList<>();

        if (!userOld.getPassword().equals(user.getPassword())) {
            sql.append(" password=?");
            list.add(user.getPassword());
        }
        if (!userOld.getName().equals(user.getName())) {
            sql.append(" name=?");
            list.add(user.getName());
        }
        if (!userOld.getEmail().equals(user.getEmail())) {
            sql.append(" email=?");
            list.add(user.getEmail());
        }

        if (list.size() < 1) throw new DaoException("Nothing to update.");

        sql.append(" where id=?");
        list.add(user.getId());

        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < list.size(); i++) {
                statement.setObject(i + 1, list.get(i));
            }
            LOG.info(statement.toString());

            int rows = statement.executeUpdate();

            if (rows < 1) throw new DaoException("User update failed.");
            if (rows > 1) throw new DaoException("Something going wrong: too many updated rows!");
        } catch (SQLException e) {
            LOG.warning("User update statement exception: " + e.getMessage());
            throw new DaoException("User update failed.", e);
        }
        return getUser(id);
    }

    @Override
    public void deleteUser(Long id) throws DaoException, IllegalArgumentException {
        if (id == null || id < 1) throw new IllegalArgumentException("Invalid id.");

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

    @Override
    public void close() throws DaoException {
        try {
            connection.close();
        } catch (SQLException e) {
            String msg = "User DAO connection close failed";
            LOG.warning(msg + ": " + e.getMessage());
            throw new DaoException(msg + ".", e);
        }
    }

}
