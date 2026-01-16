package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.entity.Owner;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OwnerDao {
    public static void createOwner(@Valid Owner owner) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(owner);
            transaction.commit();
        }
    }

    public static void updateOwner(@Valid Owner owner) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(owner);
            transaction.commit();
        }
    }

    public static Owner getOwnerById(long id) {
        Owner owner;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            owner = session.get(Owner.class, id);
        }
        if (owner == null)
            throw new EntityNotFoundException("Owner with id " + id + " not found.");
        return owner;
    }

    public static List<Owner> getAllOwners() {
        List<Owner> owners;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            owners = session.createQuery("select o from Owner o", Owner.class)
                    .getResultList();
        }
        return owners;
    }

    public static void deleteOwner(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Owner o set o.deleted = true where o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("owner with id " + id + " not found.");
            }
            transaction.commit();
        }
    }
    public static void restoreOwner(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Owner o set o.deleted = false where o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("owner with id " + id + " not found.");
            }
            transaction.commit();
        }
    }
}
