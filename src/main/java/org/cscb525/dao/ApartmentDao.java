package org.cscb525.dao;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.entity.Apartment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ApartmentDao {
    public static void createApartment(Apartment apartment) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(apartment);
            transaction.commit();
        }
    }

    public static void updateApartment(Apartment apartment) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(apartment);
            transaction.commit();
        }
    }

    public static Apartment getApartmentById(long id) {
        Apartment apartment;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            apartment = session.get(Apartment.class, id);
            transaction.commit();
        }
        return apartment;
    }

    public static List<Apartment> getAllApartments() {
        List<Apartment> apartments;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            apartments = session.createQuery("select a from Apartment a", Apartment.class)
                    .getResultList();
            transaction.commit();
        }
        return apartments;
    }

    public static void deleteApartment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("update Apartment a set a.deleted = true where a.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        }
    }
    public static void restoreApartment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("update Apartment a set a.deleted = false where a.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        }
    }
}
