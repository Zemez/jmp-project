package com.javamentor.jmp_project.config;

import com.javamentor.jmp_project.dao.UserDao;
import com.javamentor.jmp_project.exception.DbException;

import java.sql.*;
import java.util.logging.Logger;

public class MySqlConfig implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(UserDao.class.getName());
    private Connection connection;

    public MySqlConfig() throws DbException {
        connection = getMysqlConnection();

        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet resultSet = metadata.getTables(null, null, "users", null);
            if (!resultSet.first()) {
                new UserDao(connection).createTable();
            }
        } catch (SQLException e) {
            throw new DbException("Something going wrong.");
        }
    }

    public Connection getConnection() throws DbException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getMysqlConnection();
            }
            return connection;
        } catch (SQLException e) {
            throw new DbException("Connection failed.", e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            if (DriverManager.drivers().count() < 1) {
                DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
            }

            String url =
                    "jdbc:mysql://" +           //db type
                    "localhost:" +              //host name
                    "3306/" +                   //port
                    "jmp_project_db?" +         //db name
                    "user=jmp_project&" +       //login
                    "password=JMP_project123";  //password

            LOG.info(DriverManager.getDriver("jdbc:mysql://").getClass().getName());
            return DriverManager.getConnection(url);
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    @Override
    public void close() throws DbException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DbException("Closing connection failed.", e);
        }
    }
}
