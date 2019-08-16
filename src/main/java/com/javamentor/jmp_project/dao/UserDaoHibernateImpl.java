package com.javamentor.jmp_project.dao;

import com.javamentor.jmp_project.config.HibernateConfig;
import com.javamentor.jmp_project.exception.AlreadyExistsException;
import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.NotFoundException;
import com.javamentor.jmp_project.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private static final Logger LOG = Logger.getLogger(UserDaoHibernateImpl.class.getName());
    private SessionFactory sessionFactory;

    public UserDaoHibernateImpl() throws IOException {
        sessionFactory = HibernateConfig.getSessionFactory();
    }

    @Override
    public User getUser(Long id) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        } catch (HibernateException e) {
            LOG.warning(e.getClass().getName() + ": " + e.getMessage());
            throw new DaoException("User read failed.", e);
        }
    }

    @Override
    public User getUserByLogin(String login) throws DaoException, NotFoundException {
        return getUserBy("login", login);
    }

    private User getUserBy(String field, Object value) throws DaoException, NotFoundException {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from User where " + field + "=?1");
            query.setParameter(1, value);
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("User not found.", e);
        } catch (HibernateException e) {
            LOG.warning(e.getClass().getName() + ": " + e.getMessage());
            throw new DaoException("User create failed.", e);
        }
    }

    @Override
    public List<User> getAllUsers() throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User").list();
        } catch (HibernateException e) {
            LOG.warning(e.getClass().getName() + ": " + e.getMessage());
            throw new DaoException("Users read failed.", e);
        }
    }

    @Override
    public User createUser(User user) throws DaoException, AlreadyExistsException {
        try (Session session = sessionFactory.openSession()) {
            user.setId(null);
            session.save(user);
            return user;
        } catch (ConstraintViolationException e) {
            throw new AlreadyExistsException("User already exists.", e);
        } catch (HibernateException e) {
            LOG.warning(e.getClass().getName() + ": " + e.getMessage());
            throw new DaoException("User create failed.", e);
        }
    }

    @Override
    public User updateUser(User user) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            return user;
        } catch (HibernateException e) {
            LOG.warning(e.getClass().getName() + ": " + e.getMessage());
            throw new DaoException("User update failed.", e);
        }
    }

    @Override
    public void deleteUser(Long id) throws DaoException, NotFoundException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user == null) throw new NotFoundException("User not found.");
            session.delete(user);
            transaction.commit();
        } catch (HibernateException e) {
            LOG.warning(e.getClass().getName() + ": " + e.getMessage());
            throw new DaoException("User delete failed.", e);
        }
    }

    @Override
    public void close() {
//        session.close();
    }

}
