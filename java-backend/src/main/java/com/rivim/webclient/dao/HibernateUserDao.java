package com.rivim.webclient.dao;

import com.rivim.webclient.exceptions.DaoException;
import com.rivim.webclient.model.UserEntity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation with HQL
 */
@Repository
public class HibernateUserDao extends AbstractHibernateDao implements UserDao {

  private static final Logger LOG = LogManager.getLogger(HibernateUserDao.class);

  @Override
  public void create(UserEntity user) {
    try (Session session = createSession()) {
      Transaction transaction = session.beginTransaction();
      try {
        session.save(user);
        transaction.commit();

        LOG.info("{} added", user);
      } catch (RuntimeException dbException) {
        transaction.rollback();

        LOG.catching(Level.INFO, dbException);
      }
    } catch (RuntimeException e) {
      throw LOG.throwing(Level.ERROR, new DaoException("Unable to create user", e));
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void update(UserEntity user) {
    String query = "UPDATE UserEntity SET password=:password,email=:email,firstName=:firstName,lastName=:lastName,birthday=:birthday,role=:role WHERE username=:login";

    try (Session session = createSession()) {
      var updateQuery = session.createQuery(query);

      updateQuery.setParameter("password", user.getPassword());
      update(user, session, updateQuery);
    } catch (RuntimeException e) {
      throw LOG.throwing(Level.ERROR, new DaoException("Unable to update user", e));
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void updateWithoutPassword(UserEntity user) {
    String query = "UPDATE UserEntity SET email=:email,firstName=:firstName,lastName=:lastName,birthday=:birthday,role=:role WHERE username=:login";

    try (Session session = createSession()) {
      var updateQuery = session.createQuery(query);

      update(user, session, updateQuery);
    } catch (RuntimeException e) {
      throw LOG.throwing(Level.ERROR, new DaoException("Unable to update user", e));
    }
  }

  @Override
  public void remove(String login) {
    String query = "DELETE FROM UserEntity where username=:login";

    try (Session session = createSession()) {
      var deleteQuery = session.createQuery(query);

      deleteQuery.setParameter("login", login);

      Transaction transaction = session.beginTransaction();
      try {
        deleteQuery.executeUpdate();
        transaction.commit();

        LOG.info("{} removed", login);
      } catch (RuntimeException dbException) {
        transaction.rollback();

        LOG.catching(Level.INFO, dbException);
      }

    } catch (RuntimeException e) {
      throw LOG.throwing(Level.ERROR, new DaoException("Unable to remove user", e));
    }
  }

  @Override
  public List<UserEntity> findAll() {
    String query = "from UserEntity";
    List<UserEntity> users;

    try (Session session = createSession()) {
      var selectQuery = session.createQuery(query, UserEntity.class);
      users = selectQuery.getResultList();
    } catch (RuntimeException e) {
      throw LOG.throwing(Level.ERROR, new DaoException("Unable to get all users", e));
    }

    return users;
  }

  @Override
  public Optional<UserEntity> findByLogin(String login) {
    String query = "from UserEntity where login=:login";
    UserEntity user;

    try (Session session = createSession()) {
      var selectQuery = session.createQuery(query, UserEntity.class);
      selectQuery.setParameter("login", login);
      user = selectQuery.uniqueResult();
    } catch (RuntimeException e) {
      throw LOG.throwing(Level.ERROR, new DaoException("Unable to get user by login", e));
    }

    return Optional.ofNullable(user);
  }

  @Override
  public Optional<UserEntity> findByEmail(String email) {
    String query = "from UserEntity where email=:email";
    UserEntity user;

    try (Session session = createSession()) {
      var selectQuery = session.createQuery(query, UserEntity.class);
      selectQuery.setParameter("email", email);
      user = selectQuery.uniqueResult();
    } catch (RuntimeException e) {
      throw LOG.throwing(Level.ERROR, new DaoException("Unable to get user by email", e));
    }

    return Optional.ofNullable(user);
  }

  private void update(UserEntity user, Session session, Query<UserEntity> updateQuery) {
    setUserQuery(updateQuery, user);

    Transaction transaction = session.beginTransaction();
    try {
      updateQuery.executeUpdate();
      transaction.commit();

      LOG.info("{} updated", user);
    } catch (RuntimeException dbException) {
      transaction.rollback();

      LOG.catching(Level.INFO, dbException);
    }
  }

  private void setUserQuery(Query<UserEntity> userQuery, UserEntity user) {
    userQuery.setParameter("email", user.getEmail());
    userQuery.setParameter("firstName", user.getFirstName());
    userQuery.setParameter("lastName", user.getLastName());
    userQuery.setParameter("birthday", user.getBirthday());
    userQuery.setParameter("role", user.getRole());
    userQuery.setParameter("login", user.getUsername());
  }
}
