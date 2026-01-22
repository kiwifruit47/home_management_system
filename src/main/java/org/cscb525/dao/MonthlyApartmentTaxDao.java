package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.monthlyApartmentTax.*;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.MonthlyApartmentTax;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class MonthlyApartmentTaxDao {
    public static void createMonthlyApartmentTax(CreateMonthlyApartmentTaxDto taxDto) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Apartment apartment = session.get(
                    Apartment.class,
                    taxDto.getApartmentId()
            );
            if (apartment == null)
                throw new EntityNotFoundException("Apartment with id " + taxDto.getApartmentId() + " does not exist.");

            MonthlyApartmentTax monthlyApartmentTax = new MonthlyApartmentTax();
            monthlyApartmentTax.setApartment(apartment);
            monthlyApartmentTax.setPaid(taxDto.isPaid());
            monthlyApartmentTax.setDateOfPayment(taxDto.getDateOfPayment());
            monthlyApartmentTax.setPaymentValue(taxDto.getPaymentValue());
            monthlyApartmentTax.setPaymentForMonth(taxDto.getPaymentForMonth());

            session.persist(monthlyApartmentTax);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static void updateMonthlyApartmentTax(UpdateMonthlyApartmentTaxDto taxDto) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            MonthlyApartmentTax monthlyApartmentTax = session.get(
                    MonthlyApartmentTax.class,
                    taxDto.getId()
            );

            if (monthlyApartmentTax == null) {
                throw new EntityNotFoundException("Monthly apartment tax with id " + taxDto.getId() + " not found.");
            }

            monthlyApartmentTax.setDateOfPayment(taxDto.getDateOfPayment());
            monthlyApartmentTax.setPaid(taxDto.isPaid());

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static MonthlyApartmentTaxDto findMonthlyApartmentTaxById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<MonthlyApartmentTaxDto> cr = cb.createQuery(MonthlyApartmentTaxDto.class);
            Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);

            Join<?, ?> apartment = root.join("apartment");
            Join<?, ?> employee = apartment.join("employee");
            Join<?, ?> building = employee.join("building");

            cr.select(cb.construct(
                    MonthlyApartmentTaxDto.class,
                    root.get("paymentForMonth"),
                    root.get("isPaid"),
                    root.get("paymentValue"),
                    apartment.get("apartmentNumber"),
                    building.get("address")
            ))
                    .where(cb.equal(root.get("id"), id));

            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("Monthly apartment tax with id " + id + " not found.");
        }
    }

    public static List<MonthlyApartmentTaxDto> findAllMonthlyApartmentTaxes() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<MonthlyApartmentTaxDto> cr = cb.createQuery(MonthlyApartmentTaxDto.class);
            Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);

            Join<?, ?> apartment = root.join("apartment");
            Join<?, ?> employee = apartment.join("employee");
            Join<?, ?> building = employee.join("building");

            cr.select(cb.construct(
                            MonthlyApartmentTaxDto.class,
                            root.get("paymentForMonth"),
                            root.get("isPaid"),
                            root.get("paymentValue"),
                            apartment.get("apartmentNumber"),
                            building.get("address")
                    ))
                    .where(cb.isFalse(root.get("deleted")));

            return session.createQuery(cr).getResultList();
        }
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
                            cb.sum(root.get("paymentValue")),
                            BigDecimal.ZERO
                    ))
                    .where(
                            cb.and(
                                    cb.equal(root.get("apartment").get("id"), apartmentId),
                                    cb.equal(root.get("isPaid"), isPid)
                            )
                    );

            return session.createQuery(cr).getSingleResult();
        }
    }

    public static List<MonthlyApartmentTaxEmployeeDto> findMonthlyApartmentTaxesByCompany(long companyId, boolean isPaid) {
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
                    root.get("paymentForMonth"),
                    root.get("isPaid"),
                    root.get("paymentValue"),
                    apartment.get("apartmentNumber"),
                    building.get("address"),
                    employee.get("name")
            ))
                    .where(
                            cb.and(
                                    cb.equal(company.get("id"), companyId),
                                    cb.equal(root.get("isPaid"), isPaid)
                            )
                    );

            return session.createQuery(cr).getResultList();
        }
    }

    public static List<MonthlyApartmentTaxDto> findMonthlyApartmentTaxesByEmployee(long employeeId, boolean isPaid) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<MonthlyApartmentTaxDto> cr = cb.createQuery(MonthlyApartmentTaxDto.class);
            Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);

            cr.select(cb.construct(
                    MonthlyApartmentTaxDto.class,
                    root.get("paymentForMonth"),
                    root.get("isPaid"),
                    root.get("paymentValue"),
                    root.get("apartment").get("apartmentNumber"),
                    root.get("apartment").get("building").get("address")
            ))
                    .where(
                            cb.and(
                                    cb.equal(root.get("apartment").get("building").get("employee").get("id"), employeeId),
                                    cb.equal(root.get("isPaid"), isPaid)
                            )
                    );
                    return session.createQuery(cr).getResultList();
        }
    }

    public static List<MonthlyApartmentTaxDto> findMonthlyApartmentTaxesByBuilding(long buildingId, boolean isPaid) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<MonthlyApartmentTaxDto> cr = cb.createQuery(MonthlyApartmentTaxDto.class);
            Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);

            cr.select(cb.construct(
                    MonthlyApartmentTaxDto.class,
                    root.get("paymentForMonth"),
                    root.get("isPaid"),
                    root.get("paymentValue"),
                    root.get("apartment").get("apartmentNumber"),
                    root.get("apartment").get("building").get("address")
            ))
                    .where(
                            cb.and(
                                    cb.equal(root.get("apartment").get("building").get("id"), buildingId),
                                    cb.equal(root.get("isPaid"), isPaid)
                            )
                    );
            return session.createQuery(cr).getResultList();
        }
    }

    //for saving in file
    public static MonthlyApartmentTaxReceiptDto findPaidMonthlyApartmentTaxInfo(long taxId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<MonthlyApartmentTaxReceiptDto> cr = cb.createQuery(MonthlyApartmentTaxReceiptDto.class);
            Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);

            Join<?, ?> apartment = root.join("apartment");
            Join<?, ?> building = apartment.join("building");
            Join<?, ?> employee = building.join("employee");
            Join<?, ?> company = employee.join("company");

            cr.select(cb.construct(
                    MonthlyApartmentTaxReceiptDto.class,
                    root.get("paymentForMonth"),
                    root.get("dateOfPayment"),
                    root.get("paymentValue"),
                    apartment.get("apartmentNumber"),
                    building.get("address"),
                    employee.get("name"),
                    company.get("name")
            ))
                    .where(cb.equal(root.get("id"), taxId));
            return session.createQuery(cr).getSingleResult();
        }
    }
    
    public static boolean findMonthlyApartmentTaxPaymentStatus(long taxId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Boolean> cr = cb.createQuery(Boolean.class);
            Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);
            
            cr.select(cb.construct(
                    Boolean.class,
                    root.get("isPaid")
            ))
                    .where(cb.equal(root.get("id"), taxId));
            
            return session.createQuery(cr).getSingleResult();
        }
    }

}
