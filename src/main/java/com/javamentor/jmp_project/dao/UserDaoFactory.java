package com.javamentor.jmp_project.dao;

import com.javamentor.jmp_project.config.DbHelper;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoFactory {

    private static final Logger LOG = Logger.getLogger(UserDaoFactory.class.getName());

    private UserDaoFactory() {
    }

    public static UserDao getUserDao() {
        Properties properties = new Properties();

        try {
            properties.load(Objects.requireNonNull(UserDaoFactory.class.getClassLoader().getResourceAsStream("database.properties")));
        } catch (IOException e) {
            LOG.log(Level.CONFIG, "Database properties load failed: " + e.getMessage());
            return null;
        }

        switch (properties.getProperty("database.type").toLowerCase()) {
            case "hibernate":
                return new UserDaoHibernateImpl(DbHelper.getConfiguration());
            case "jdbc":
                return new UserDaoJdbcImpl(DbHelper.getConnection());
            default:
                return null;
        }
    }

}
