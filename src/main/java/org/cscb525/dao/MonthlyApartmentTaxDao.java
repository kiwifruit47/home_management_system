package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxDto;
import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxEmployeeDto;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.MonthlyApartmentTax;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
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

    public static BigDecimal sumMonthlyApartmentTaxesSumByApartment(long apartmentId, boolean isPid) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BigDecimal> cr = cb.createQuery(BigDecimal.class);
            Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);

            cr.select(cb.coalesce(
                            cb.sum(root.get("payment_value")),
                            BigDecimal.ZERO
                    ))
                    .where(
                            cb.and(
                                    cb.equal(root.get("apartment").get("id"), apartmentId),
                                    cb.equal(root.get("is_paid"), isPid)
                            )
                    );

            return session.createQuery(cr).getSingleResult();
        }
    }

    public static List<MonthlyApartmentTaxEmployeeDto> getMonthlyApartmentTaxesByCompany(long companyId, boolean isPaid) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<MonthlyApartmentTaxEmployeeDto> cr = cb.createQuery(MonthlyApartmentTaxEmployeeDto.class);
            Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);

            Join<?, ?> apartment = root.join("apartment");
            Join<?, ?> building = apartment.join("building");
            Join<?, ?> employee = building.join("employee");
            Join<?, ?> company = employee.join("company");

            cr.select(cb.construct(
                    MonthlyApartmentTaxEmployeeDto.class,
                    root.get("payment_for_month"),
                    root.get("is_paid"),
                    root.get("date_of_payment"),
                    root.get("payment_value"),
                    apartment.get("apartment_number"),
                    building.get("address"),
                    employee.get("name")
            ))
                    .where(
                            cb.and(
                                    cb.equal(company.get("id"), companyId),
                                    cb.equal(root.get("is_paid"), isPaid)
                            )
                    );

            return session.createQuery(cr).getResultList();
        }
    }

    public static List<MonthlyApartmentTaxDto> getMonthlyApartmentTaxesByEmployee(long employeeId, boolean isPaid) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<MonthlyApartmentTaxDto> cr = cb.createQuery(MonthlyApartmentTaxDto.class);
            Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);

            cr.select(cb.construct(
                    MonthlyApartmentTaxDto.class,
                    root.get("payment_for_month"),
                    root.get("is_paid"),
                    root.get("date_of_payment"),
                    root.get("payment_value"),
                    root.get("apartment").get("apartment_number"),
                    root.get("apartment").get("building").get("address")
            ))
                    .where(
                            cb.and(
                                    cb.equal(root.get("apartment").get("building").get("employee").get("id"), employeeId),
                                    cb.equal(root.get("is_paid"), isPaid)
                            )
                    );
                    return session.createQuery(cr).getResultList();
        }
    }

    public static List<MonthlyApartmentTaxDto> getMonthlyApartmentTaxesByBuilding(long buildingId, boolean isPaid) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<MonthlyApartmentTaxDto> cr = cb.createQuery(MonthlyApartmentTaxDto.class);
            Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);

            cr.select(cb.construct(
                    MonthlyApartmentTaxDto.class,
                    root.get("payment_for_month"),
                    root.get("is_paid"),
                    root.get("date_of_payment"),
                    root.get("payment_value"),
                    root.get("apartment").get("apartment_number"),
                    root.get("apartment").get("building").get("address")
            ))
                    .where(
                            cb.and(
                                    cb.equal(root.get("apartment").get("building").get("id"), buildingId),
                                    cb.equal(root.get("is_paid"), isPaid)
                            )
                    );
            return session.createQuery(cr).getResultList();
        }
    }


}
