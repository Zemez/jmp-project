package com.javamentor.jmp_project.config;

import com.javamentor.jmp_project.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateConfig {

    private static final Logger LOG = Logger.getLogger(HibernateConfig.class.getName());

    private static SessionFactory sessionFactory;

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            sessionFactory = createSessionFactory();
            LOG.info("New session factory created.");
        }
        return sessionFactory;
    }

    private static SessionFactory createSessionFactory() {
        Properties properties = new Properties();

        try {
            properties.load(HibernateConfig.class.getClassLoader().getResourceAsStream("hibernate.properties"));
        } catch (IOException e) {
            LOG.log(Level.CONFIG, "Hibernate properties load failed: " + e.getMessage());
            return null;
        }

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
//        configuration.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
        configuration.addAnnotatedClass(User.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

}
