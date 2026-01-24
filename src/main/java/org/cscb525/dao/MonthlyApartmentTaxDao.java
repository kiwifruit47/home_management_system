package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.monthlyApartmentTax.*;
import org.cscb525.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
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
            monthlyApartmentTax.setPaid(false);
            monthlyApartmentTax.setDateOfPayment(null);
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

    public static void markTaxAsPaid(long taxId, LocalDate dateOfPayment) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            MonthlyApartmentTax monthlyApartmentTax = session.get(
                    MonthlyApartmentTax.class,
                    taxId
            );

            if (monthlyApartmentTax == null) {
                throw new EntityNotFoundException("Monthly apartment tax with id " + taxId + " not found.");
            }

            monthlyApartmentTax.setDateOfPayment(dateOfPayment);
            monthlyApartmentTax.setPaid(true);

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

            Join<MonthlyApartmentTax, Apartment> apartment = root.join("apartment");
            Join<Apartment, Building> building = apartment.join("building");

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

            Join<MonthlyApartmentTax, Apartment> apartment = root.join("apartment");
            Join<Apartment, Building> building = apartment.join("building");
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

    public static BigDecimal sumMonthlyApartmentTaxesByApartment(long apartmentId, boolean isPid) {
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

            Join<MonthlyApartmentTax, Apartment> apartment = root.join("apartment");
            Join<Apartment, Building> building = apartment.join("building");
            Join<Building, Employee> employee = building.join("employee");
            Join<Employee, Company> company = employee.join("company");

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

            Join<MonthlyApartmentTax, Apartment> apartment = root.join("apartment");
            Join<Apartment, Building> building = apartment.join("building");
            Join<Building, Employee> employee = building.join("employee");
            Join<Employee, Company> company = employee.join("company");

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

    public static MonthlyApartmentTaxReceiptDto findPaidMonthlyApartmentTaxById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
             CriteriaBuilder cb = session.getCriteriaBuilder();
             CriteriaQuery<MonthlyApartmentTaxReceiptDto> cr = cb.createQuery(MonthlyApartmentTaxReceiptDto.class);
             Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);

             Join<MonthlyApartmentTax, Apartment> apartment = root.join("apartment");
             Join<Apartment, Building> building = apartment.join("building");
             Join<Building, Employee> employee = building.join("employee");
             Join<Employee, Company> company = employee.join("company");

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
                     .where(cb.and(
                             cb.equal(root.get("id"), id),
                             cb.isTrue(root.get("isPaid"))
                     ));
             return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("Monthly apartment tax with id " + id + " not found.");
        }
    }

    public static CalculateMonthlyApartmentTaxDto findDataForTaxCalc(long apartmentId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CalculateMonthlyApartmentTaxDto> cr = cb.createQuery(CalculateMonthlyApartmentTaxDto.class);
            Root<Apartment> root = cr.from(Apartment.class);

            Join<Apartment, Building> building = root.join("building");
            Join<Apartment, Occupant> occupants = root.join("occupants", JoinType.LEFT);

            occupants.on(
                    cb.and(
                            cb.greaterThan(occupants.get("age"), 7),
                            cb.isTrue(occupants.get("usesElevator"))
                    )
            );

            cr.select(cb.construct(
                    CalculateMonthlyApartmentTaxDto.class,
                    root.get("area"),
                    root.get("pets"),
                    cb.count(occupants),
                    building.get("monthlyTaxPerPerson"),
                    building.get("monthlyTaxPerPet"),
                    building.get("monthlyTaxPerM2")
            ))
                    .where(cb.equal(root.get("id"), apartmentId))
                    .groupBy(
                            root.get("area"),
                            root.get("pets"),
                            building.get("monthlyTaxPerPerson"),
                            building.get("monthlyTaxPerPet"),
                            building.get("monthlyTaxPerM2")
                    );
            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("Apartment with id " + apartmentId + " not found.");
        }
    }

    public static boolean taxExistenceCheck(Long apartmentId, YearMonth month) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<MonthlyApartmentTax> root = cr.from(MonthlyApartmentTax.class);

            cr.select(cb.count(root))
                    .where(
                            cb.and(
                                    cb.equal(root.get("apartment").get("id"), apartmentId),
                                    cb.equal(root.get("paymentForMonth"), month)
                            )
                    );

            long count = session.createQuery(cr).getSingleResult();
            return count > 0;
        }
    }
}
