package com.rivim.webclient.dao;

import com.rivim.webclient.exceptions.DaoException;
import com.rivim.webclient.model.RoleEntity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

/**
 * Implementation with Criteria API
 */
@Repository
public class HibernateRoleDao extends AbstractHibernateDao implements RoleDao {

  private static final Logger LOG = LogManager.getLogger(HibernateRoleDao.class);

  @Override
  public void create(RoleEntity role) {
    try (Session session = createSession()) {
      Transaction transaction = session.beginTransaction();
      try {
        session.save(role);
        transaction.commit();

        LOG.info("{} added", role);
      } catch (RuntimeException dbException) {
        transaction.rollback();

        LOG.catching(Level.INFO, dbException);
      }
    } catch (RuntimeException e) {
      throw LOG.throwing(Level.ERROR, new DaoException("Unable to create role", e));
    }
  }

  @Override
  public void update(RoleEntity role) {
    try (Session session = createSession()) {
      var builder = session.getCriteriaBuilder();
      var updateCriteria = builder.createCriteriaUpdate(RoleEntity.class);
      var root = updateCriteria.from(RoleEntity.class);

      updateCriteria.set("name", role.getName());
      updateCriteria.where(builder.equal(root.get("id"), role.getId()));
      Query updateQuery = session.createQuery(updateCriteria);

      Transaction transaction = session.beginTransaction();
      try {
        updateQuery.executeUpdate();
        transaction.commit();

        LOG.info("{} updated", role);
      } catch (RuntimeException dbException) {
        transaction.rollback();

        LOG.catching(Level.INFO, dbException);
      }
    } catch (RuntimeException e) {
      throw LOG.throwing(Level.ERROR, new DaoException("Unable to remove role", e));
    }
  }

  @Override
  public void remove(RoleEntity role) {
    try (Session session = createSession()) {
      var builder = session.getCriteriaBuilder();
      var deleteCriteria = builder.createCriteriaDelete(RoleEntity.class);
      var root = deleteCriteria.from(RoleEntity.class);

      deleteCriteria.where(builder.equal(root.get("id"), role.getId()));
      Query deleteQuery = session.createQuery(deleteCriteria);

      Transaction transaction = session.beginTransaction();
      try {
        deleteQuery.executeUpdate();
        transaction.commit();

        LOG.info("{} deleted", role);
      } catch (RuntimeException dbException) {
        transaction.rollback();

        LOG.catching(Level.INFO, dbException);
      }
    } catch (RuntimeException e) {
      throw LOG.throwing(Level.ERROR, new DaoException("Unable to remove role", e));
    }
  }

  @Override
  public List<RoleEntity> findAll() {
    List<RoleEntity> roles;

    try (Session session = createSession()) {
      var builder = session.getCriteriaBuilder();
      var selectQuery = builder.createQuery(RoleEntity.class);
      var root = selectQuery.from(RoleEntity.class);

      selectQuery.select(root);
      roles = session.createQuery(selectQuery).getResultList();
    } catch (RuntimeException e) {
      throw LOG.throwing(Level.ERROR, new DaoException("Unable to get all roles", e));
    }

    return roles;
  }

  @Override
  public Optional<RoleEntity> findByName(String name) {
    RoleEntity role;

    try (Session session = createSession()) {
      var builder = session.getCriteriaBuilder();
      var selectQuery = builder.createQuery(RoleEntity.class);
      var root = selectQuery.from(RoleEntity.class);

      selectQuery.select(root)
          .where(builder.equal(root.get("name"), name));
      role = session.createQuery(selectQuery).uniqueResult();
    } catch (RuntimeException e) {
      throw LOG.throwing(Level.ERROR, new DaoException("Unable to role by name", e));
    }

    return Optional.ofNullable(role);
  }
}
