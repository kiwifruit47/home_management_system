package org.cscb525.dao;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.entity.Owner;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OwnerDao {
    public static void createOwner(Owner owner) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(owner);
            transaction.commit();
        }
    }

    public static void updateOwner(Owner owner) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(owner);
            transaction.commit();
        }
    }

    public static Owner getOwnerById(long id) {
        Owner owner;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            owner = session.get(Owner.class, id);
            transaction.commit();
        }
        return owner;
    }

    public static List<Owner> getAllOwners() {
        List<Owner> owners;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            owners = session.createQuery("select o from Owner o", Owner.class)
                    .getResultList();
            transaction.commit();
        }
        return owners;
    }

    public static void deleteOwner(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("update Owner o set o.deleted = true where o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        }
    }
    public static void restoreOwner(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("update Owner o set o.deleted = false where o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        }
    }
}
