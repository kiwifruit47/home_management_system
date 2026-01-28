package dao.monthlyApartmentTax;

import org.cscb525.config.SessionFactoryUtil;

import org.cscb525.dao.MonthlyApartmentTaxDao;
import org.cscb525.dto.monthlyApartmentTax.CreateMonthlyApartmentTaxDto;
import org.cscb525.entity.*;
import org.cscb525.exceptions.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonthlyApartmentTaxCreateIT {
    @BeforeEach
    void setUp() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company company = new Company("Company");
            session.persist(company);

            Employee employee = new Employee("Gosho", company);
            session.persist(employee);

            Building building = new Building(
                    "ul. nqkoq 100",
                    8,
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.01),
                    employee
            );
            session.persist(building);

            Apartment apartment = new Apartment(
                    1,
                    1,
                    BigDecimal.valueOf(100),
                    1,
                    building
            );
            session.persist(apartment);

            tx.commit();
        }
    }

    @AfterEach
    void cleanDB() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from MonthlyApartmentTax").executeUpdate();
            session.createMutationQuery("delete from Apartment").executeUpdate();
            session.createMutationQuery("delete from Building").executeUpdate();
            session.createMutationQuery("delete from Employee").executeUpdate();
            session.createMutationQuery("delete from Company").executeUpdate();
            tx.commit();
        }
    }

    @Test
    public void createMonthlyApartmentTax_success() {
        long apartmentId;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartmentId = session.createQuery("select a.id from Apartment a", Long.class)
                    .getSingleResult();
        }

        CreateMonthlyApartmentTaxDto dto = new CreateMonthlyApartmentTaxDto(
                YearMonth.now(),
                BigDecimal.valueOf(30),
                apartmentId
        );

        MonthlyApartmentTaxDao.createMonthlyApartmentTax(dto);

        MonthlyApartmentTax tax;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tax = session.createQuery("from MonthlyApartmentTax", MonthlyApartmentTax.class)
                    .getSingleResult();
        }

        assertEquals(YearMonth.now(), tax.getPaymentForMonth());
        assertEquals(0, BigDecimal.valueOf(30).compareTo(tax.getPaymentValue()));
        assertEquals(apartmentId, tax.getApartment().getId());
    }

    @Test
    public void createMonthlyApartmentTax_apartmentNotFound_throwsException() {
        CreateMonthlyApartmentTaxDto dto = new CreateMonthlyApartmentTaxDto(
                YearMonth.now(),
                BigDecimal.valueOf(30),
                5L
        );

        assertThrows(
                NotFoundException.class,
                () -> MonthlyApartmentTaxDao.createMonthlyApartmentTax(dto)
        );
    }

    @Test
    void MonthlyApartmentTax_deletedApartment_throwsException() {
        Apartment apartment;
        long apartmentId;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartment = session.createQuery("from Apartment a", Apartment.class)
                    .getSingleResult();
            apartmentId = apartment.getId();
            Transaction transaction = session.beginTransaction();
            apartment.setDeleted(true);
            transaction.commit();
        }

        CreateMonthlyApartmentTaxDto dto = new CreateMonthlyApartmentTaxDto(
                YearMonth.now(),
                BigDecimal.valueOf(30),
                apartmentId
        );

        assertThrows(
                NotFoundException.class,
                () -> MonthlyApartmentTaxDao.createMonthlyApartmentTax(dto)
        );
    }
}
