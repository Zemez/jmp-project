package com.javamentor.jmp_project.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcConfig {

    private static final Logger LOG = Logger.getLogger(JdbcConfig.class.getName());

    private static String connUrl;
    private static String connUser;
    private static String connPass;

    static {
        Properties properties = new Properties();

        try {
            properties.load(Objects.requireNonNull(JdbcConfig.class.getClassLoader().getResourceAsStream("jdbc.properties")));

            connUrl = properties.getProperty("connection.url");
            connUser = properties.getProperty("connection.user");
            connPass = properties.getProperty("connection.password");

            try {
                Class.forName(properties.getProperty("connection.driver"));
            } catch (ClassNotFoundException e) {
                LOG.log(Level.CONFIG, "JDBC driver initialization failed: " + e.getMessage());
            }

            LOG.info("JDBC driver successful initialized.");
        } catch (IOException e) {
            LOG.log(Level.CONFIG, "JDBC properties load failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(connUrl, connUser, connPass);
        } catch (SQLException e) {
            String msg = "JDBC get connection failed";
            LOG.warning(msg + ": " + e.getMessage());
            throw new IllegalStateException(msg + ".", e);
        }
    }

}
