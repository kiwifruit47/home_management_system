package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.Building;
import org.cscb525.entity.MonthlyApartmentTax;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class ApartmentDao {

    //apartment can't be persisted if Building doesn't exist in the DB
    public static void createApartment(@Valid Apartment apartment) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Building building = session.get(
                    Building.class,
                    apartment.getBuilding().getId()
            );

            if (building == null) {
                throw new EntityNotFoundException(
                        "Building with id " + apartment.getBuilding().getId() + " does not exist."
                );
            }
            session.persist(apartment);
            transaction.commit();
        }
    }

    public static void updateApartment(@Valid Apartment apartment) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Building building = session.get(
                    Building.class,
                    apartment.getBuilding().getId()
            );

            if (building == null) {
                throw new EntityNotFoundException(
                        "Building with id " + apartment.getBuilding().getId() + " does not exist."
                );
            }
            session.merge(apartment);
            transaction.commit();
        }
    }

    public static Apartment getApartmentById(long id) {
        Apartment apartment;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartment = session.get(Apartment.class, id);
        }
        if (apartment == null)
            throw new EntityNotFoundException("Apartment with id " + id + " not found.");
        return apartment;
    }

    public static List<Apartment> getAllApartments() {
        List<Apartment> apartments;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartments = session.createQuery("select a from Apartment a", Apartment.class)
                    .getResultList();
        }
        return apartments;
    }

    public static void deleteApartment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Apartment a set a.deleted = true where a.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Apartment with id " + id + " not found.");
            }
            transaction.commit();
        }
    }
    public static void restoreApartment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Apartment a set a.deleted = false where a.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Apartment with id " + id + " not found.");
            }
            transaction.commit();
        }
    }

    public static List<MonthlyApartmentTax> getAllMonthlyApartmentTaxesByApartment(Apartment apartment) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<MonthlyApartmentTax> cr = cb.createQuery(MonthlyApartmentTax.class);
            Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);

            cr.select(root).where(cb.equal(root.get("apartment"), apartment));
            return session.createQuery(cr).getResultList();
        }
    }

}
