package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.MonthlyApartmentTax;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MonthlyApartmentTaxDao {
    public static void createMonthlyApartmentTax(MonthlyApartmentTax monthlyApartmentTax) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Apartment apartment = session.get(
                    Apartment.class,
                    monthlyApartmentTax.getApartment().getId()
            );
            if (apartment == null)
                throw new EntityNotFoundException("Apartment with id " + monthlyApartmentTax.getApartment().getId() + " does not exist.");

            session.persist(monthlyApartmentTax);
            transaction.commit();
        }
    }

    public static void updateMonthlyApartmentTax(MonthlyApartmentTax monthlyApartmentTax) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Apartment apartment = session.get(
                    Apartment.class,
                    monthlyApartmentTax.getApartment().getId()
            );
            if (apartment == null)
                throw new EntityNotFoundException("Apartment with id " + monthlyApartmentTax.getApartment().getId() + " does not exist.");

            session.merge(monthlyApartmentTax);
            transaction.commit();
        }
    }

    public static MonthlyApartmentTax getMonthlyApartmentTaxById(long id) {
        MonthlyApartmentTax monthlyApartmentTax;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            monthlyApartmentTax = session.get(MonthlyApartmentTax.class, id);
        }
        if (monthlyApartmentTax == null)
            throw new EntityNotFoundException("Monthly apartment tax with id " + id + " not found.");
        return monthlyApartmentTax;
    }

    public static List<MonthlyApartmentTax> getAllMonthlyApartmentTaxes() {
        List<MonthlyApartmentTax> monthlyApartmentTaxes;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            monthlyApartmentTaxes = session.createQuery("select m from MonthlyApartmentTax m", MonthlyApartmentTax.class)
                    .getResultList();
            transaction.commit();
        }
        return monthlyApartmentTaxes;
    }

    public static void deleteMonthlyApartmentTax(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update MonthlyApartmentTax m set m.deleted = true where m.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Monthly apartment tax with id " + id + " not found.");
            }
            transaction.commit();
        }
    }
    public static void restoreMonthlyApartmentTax(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update MonthlyApartmentTax m set m.deleted = false where m.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Monthly apartment tax with id " + id + " not found.");
            }
            transaction.commit();
        }
    }
}
