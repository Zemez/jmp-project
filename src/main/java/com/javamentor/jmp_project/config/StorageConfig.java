package com.javamentor.jmp_project.config;

import com.javamentor.jmp_project.dao.DaoFactory;
import com.javamentor.jmp_project.dao.HibernateDaoFactory;
import com.javamentor.jmp_project.dao.JdbcDaoFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageConfig {

    private static final Logger LOG = Logger.getLogger(StorageConfig.class.getName());

    private static DaoFactory daoFactory;

    private StorageConfig() {
    }

    public static DaoFactory getDaoFactory() {
        if (daoFactory == null) {
            Properties properties = new Properties();

            try {
                properties.load(Objects.requireNonNull(StorageConfig.class.getClassLoader().getResourceAsStream("storage.properties")));
            } catch (IOException e) {
                LOG.log(Level.CONFIG, "Storage properties load failed: " + e.getMessage());
            }

            switch (properties.getProperty("storage.type").toLowerCase()) {
                case "hibernate":
                    daoFactory = new HibernateDaoFactory();
                    LOG.info("Hibernate storage in use now.");
                    break;
                case "jdbc":
                    daoFactory = new JdbcDaoFactory();
                    LOG.info("JDBC storage in use now.");
                    break;
                default:
                    daoFactory = null;
                    LOG.warning("No storage in use now!");
            }
        }
        return daoFactory;
    }
}
