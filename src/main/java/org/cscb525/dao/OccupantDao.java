package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.Occupant;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OccupantDao {
    //occupant can't be persisted if the apartment they live in doesn't exist in the DB
    public static void createOccupant(Occupant occupant) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Apartment apartment = session.get(
                    Apartment.class,
                    occupant.getApartment().getId()
            );
            if (apartment == null)
                throw new EntityNotFoundException("Apartment with id " + occupant.getApartment().getId() + " does not exist.");

            session.persist(occupant);
            transaction.commit();
        }
    }

    public static void updateOccupant(Occupant occupant) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Apartment apartment = session.get(
                    Apartment.class,
                    occupant.getApartment().getId()
            );
            if (apartment == null)
                throw new EntityNotFoundException("Apartment with id " + occupant.getApartment().getId() + " does not exist.");

            session.merge(occupant);
            transaction.commit();
        }
    }

    public static Occupant getOccupantById(long id) {
        Occupant occupant;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            occupant = session.get(Occupant.class, id);
        }
        if (occupant == null)
            throw new EntityNotFoundException("Occupant with id " + id + " not found.");
        return occupant;
    }

    public static List<Occupant> getAllOccupants() {
        List<Occupant> occupants;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            occupants = session.createQuery("select o from Occupant o", Occupant.class)
                    .getResultList();
        }
        return occupants;
    }

    public static void deleteOccupant(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Occupant o set o.deleted = true where o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Occupant with id " + id + " not found.");
            }
            transaction.commit();
        }
    }
    public static void restoreOccupant(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Occupant o set o.deleted = false where o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Occupant with id " + id + " not found.");
            }
            transaction.commit();
        }
    }
}
