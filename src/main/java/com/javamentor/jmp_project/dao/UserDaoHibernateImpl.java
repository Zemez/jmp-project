package com.javamentor.jmp_project.dao;

import com.javamentor.jmp_project.exception.AlreadyExistsException;
import com.javamentor.jmp_project.exception.DaoException;
import com.javamentor.jmp_project.exception.InvalidArgumentException;
import com.javamentor.jmp_project.exception.NotFoundException;
import com.javamentor.jmp_project.model.User;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private static final Logger LOG = Logger.getLogger(UserDaoHibernateImpl.class.getName());
    private SessionFactory sessionFactory;

    public UserDaoHibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User getUser(Long id) throws DaoException, InvalidArgumentException {
        if (id == null || id <= 0) throw new InvalidArgumentException("Invalid user id.");

        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        } catch (HibernateException e) {
            LOG.warning("User read failed: " + e.getMessage());
            throw new DaoException("User read failed.", e);
        }
    }

    @Override
    public User getUserByLogin(String login) throws DaoException, InvalidArgumentException, NotFoundException {
        if (StringUtils.isBlank(login)) throw new InvalidArgumentException("Invalid user login.");

        List<User> users = getUsersBy("login", login);
        return users.size() > 0 ? users.get(0) : null;
    }

    private List<User> getUsersBy(String field, Object value) throws DaoException, NotFoundException {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where " + field + "=:value", User.class);
            query.setParameter("value", value);
            return query.getResultList();
        } catch (NoResultException e) {
            throw new NotFoundException("Users not found.", e);
        } catch (HibernateException e) {
            LOG.warning("Users read failed: " + e.getMessage());
            throw new DaoException("Users read failed.", e);
        }
    }

    @Override
    public List<User> getAllUsers() throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (HibernateException e) {
            LOG.warning("Users read failed: " + e.getMessage());
            throw new DaoException("Users read failed.", e);
        }
    }

    @Override
    public User createUser(User user) throws DaoException, InvalidArgumentException, AlreadyExistsException {
        if (user == null) throw new InvalidArgumentException("Invalid null user.");

        try (Session session = sessionFactory.openSession()) {
            if (user.getId() != null) user.setId(null);
            session.save(user);
            return user;
        } catch (ConstraintViolationException e) {
            throw new AlreadyExistsException("User already exists.", e);
        } catch (HibernateException e) {
            LOG.warning("User create failed: " + e.getMessage());
            throw new DaoException("User create failed.", e);
        }
    }

    @Override
    public User updateUser(User user) throws DaoException, InvalidArgumentException, NotFoundException {
        if (user == null) throw new InvalidArgumentException("Invalid null user.");
        if (user.getId() == null || user.getId() <= 0) throw new InvalidArgumentException("Invalid user id.");

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            if (session.get(User.class, user.getId()) == null) throw new NotFoundException("User not found.");
            session.merge(user);
            transaction.commit();
            return user;
        } catch (OptimisticLockException e) {
            if (transaction != null) transaction.rollback();
            throw new NotFoundException("User was deleted.", e);
        } catch (HibernateException e) {
            LOG.warning("User update failed: " + e.getMessage());
            if (transaction != null) transaction.rollback();
            throw new DaoException("User update failed.", e);
        }
    }

    @Override
    public void deleteUser(Long id) throws DaoException, InvalidArgumentException, NotFoundException {
        if (id == null || id <= 0) throw new InvalidArgumentException("Invalid user id.");

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user == null) throw new NotFoundException("User not found.");
            session.delete(user);
            transaction.commit();
        } catch (IllegalArgumentException e) {
            if (transaction != null) transaction.rollback();
            throw new InvalidArgumentException("Invalid user id.", e);
        } catch (HibernateException e) {
            LOG.warning("User delete failed: " + e.getMessage());
            if (transaction != null) transaction.rollback();
            throw new DaoException("User delete failed.", e);
        }
    }

}
