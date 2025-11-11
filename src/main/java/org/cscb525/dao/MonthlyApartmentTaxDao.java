package org.cscb525.dao;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.entity.MonthlyApartmentTax;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MonthlyApartmentTaxDao {
    public static void createMonthlyApartmentTax(MonthlyApartmentTax monthlyApartmentTax) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(monthlyApartmentTax);
            transaction.commit();
        }
    }

    public static void updateMonthlyApartmentTax(MonthlyApartmentTax monthlyApartmentTax) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(monthlyApartmentTax);
            transaction.commit();
        }
    }

    public static MonthlyApartmentTax getMonthlyApartmentTaxById(long id) {
        MonthlyApartmentTax monthlyApartmentTax;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            monthlyApartmentTax = session.get(MonthlyApartmentTax.class, id);
            transaction.commit();
        }
        return monthlyApartmentTax;
    }

    public static List<MonthlyApartmentTax> getAllMonthlyApartmentTaxs() {
        List<MonthlyApartmentTax> monthlyApartmentTaxs;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            monthlyApartmentTaxs = session.createQuery("select m from MonthlyApartmentTax m", MonthlyApartmentTax.class)
                    .getResultList();
            transaction.commit();
        }
        return monthlyApartmentTaxs;
    }

    public static void deleteMonthlyApartmentTax(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("update MonthlyApartmentTax m set m.deleted = true where m.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        }
    }
    public static void restoreMonthlyApartmentTax(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("update MonthlyApartmentTax m set m.deleted = false where m.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        }
    }
}
