package org.cscb525.dao;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.entity.Occupant;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OccupantDao {
    public static void createOccupant(Occupant occupant) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(occupant);
            transaction.commit();
        }
    }

    public static void updateOccupant(Occupant occupant) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(occupant);
            transaction.commit();
        }
    }

    public static Occupant getOccupantById(long id) {
        Occupant occupant;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            occupant = session.get(Occupant.class, id);
            transaction.commit();
        }
        return occupant;
    }

    public static List<Occupant> getAllOccupants() {
        List<Occupant> occupants;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            occupants = session.createQuery("select o from Occupant o", Occupant.class)
                    .getResultList();
            transaction.commit();
        }
        return occupants;
    }

    public static void deleteOccupant(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("update Occupant o set o.deleted = true where o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        }
    }
    public static void restoreOccupant(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("update Occupant o set o.deleted = false where o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        }
    }
}
