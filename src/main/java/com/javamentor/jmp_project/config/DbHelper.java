package com.javamentor.jmp_project.config;

import org.hibernate.SessionFactory;

import java.sql.Connection;

public class DbHelper {

    private DbHelper() {
    }

    public static Connection getConnection() {
        return JdbcConfig.getConnection();
    }

    public static SessionFactory getConfiguration() {
        return HibernateConfig.getSessionFactory();
    }

}
