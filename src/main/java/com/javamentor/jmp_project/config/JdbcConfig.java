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
    private static final Properties PROP = new Properties();

    static {
        try {
            PROP.load(Objects.requireNonNull(JdbcConfig.class.getClassLoader().getResourceAsStream("jdbc.properties")));
        } catch (IOException e) {
            LOG.log(Level.CONFIG, "JDBC properties load failed: " + e.getMessage());
        }
    }

    private static final String JDBC_VENDOR = PROP.getProperty("jdbc.vendor");
    private static final String JDBC_DRIVER = "com." + JDBC_VENDOR + ".jdbc.Driver";
    private static final String JDBC_PROTO = "jdbc:" + JDBC_VENDOR + ":";
    private static final String JDBC_HOST = PROP.getProperty("jdbc.host");
    private static final String JDBC_FAIL = PROP.getProperty("jdbc.failover");
    private static final String JDBC_PORT = PROP.getProperty("jdbc.port");
    private static final String JDBC_DB = PROP.getProperty("jdbc.db");
    private static final String JDBC_URL = JDBC_PROTO +
            "//" +
            (JDBC_HOST != null ? JDBC_HOST : "") +
            (JDBC_FAIL != null ? "," + JDBC_FAIL : "") +
            (JDBC_PORT != null ? ':' + JDBC_PORT : "") +
            '/' +
            (JDBC_DB != null ? JDBC_DB : "");
    private static final String JDBC_USER = PROP.getProperty("jdbc.user");
    private static final String JDBC_PASS = PROP.getProperty("jdbc.password");

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            LOG.log(Level.CONFIG, "JDBC driver initialization failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            LOG.info("url: " + JDBC_URL);
            LOG.info("user: " + JDBC_USER);
            LOG.info("pass: " + JDBC_PASS);
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
        } catch (SQLException e) {
            String msg = "JDBC get connection failed";
            LOG.warning(msg + ": " + e.getMessage());
            throw new IllegalStateException(msg + ".", e);
        }
    }

}
