package dao.monthlyApartmentTax;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.MonthlyApartmentTaxDao;
import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxDto;
import org.cscb525.dto.occupant.CreateOccupantDto;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.Building;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.cscb525.exceptions.NotFoundException;
import org.cscb525.service.ApartmentService;
import org.cscb525.service.MonthlyApartmentTaxService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SingleMonthlyApartmentTaxIT {
    @BeforeEach
    void setup() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company c1 = new Company("Company1");
            session.persist(c1);

            Company c2 = new Company("Company2");
            session.persist(c2);

            Company c3 = new Company("Company3");
            session.persist(c3);

            Employee e1 = new Employee("Sasho", c1);
            session.persist(e1);

            Employee e2 = new Employee("Gosho", c2);
            session.persist(e2);

            Employee e3 = new Employee("Pesho", c3);
            session.persist(e3);

            Building b1 = new Building(
                    "ul. nqkoq 100",
                    8,
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.01),
                    e1
            );
            session.persist(b1);

            Building b2 = new Building(
                    "ul. nqkoq 101",
                    8,
                    BigDecimal.valueOf(20),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.01),
                    e2
            );
            session.persist(b2);

            Building b3 = new Building(
                    "ul. nqkoq 102",
                    8,
                    BigDecimal.valueOf(30),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.01),
                    e3
            );
            session.persist(b3);

            Apartment a1 = new Apartment(
                    1,
                    1,
                    BigDecimal.valueOf(100),
                    1,
                    b1
            );
            session.persist(a1);

            Apartment a2 = new Apartment(
                    1,
                    1,
                    BigDecimal.valueOf(100),
                    1,
                    b2
            );
            session.persist(a2);

            Apartment a3 = new Apartment(
                    1,
                    1,
                    BigDecimal.valueOf(100),
                    1,
                    b3
            );
            session.persist(a3);

            transaction.commit();
        }

        ApartmentService apartmentService = new ApartmentService();
        CreateOccupantDto occupantDto = new CreateOccupantDto(20, "Ivan", true);

        apartmentService.addOccupantToApartment(1, occupantDto);
        apartmentService.addOccupantToApartment(2, occupantDto);
        apartmentService.addOccupantToApartment(3, occupantDto);

        MonthlyApartmentTaxService.generateMonthlyTaxesForCurrentMonth();
    }

    @AfterEach
    void cleanDB() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createMutationQuery("delete from MonthlyApartmentTax").executeUpdate();
            session.createMutationQuery("delete from Occupant").executeUpdate();
            session.createMutationQuery("delete from Apartment").executeUpdate();
            session.createMutationQuery("delete from Building").executeUpdate();
            session.createMutationQuery("delete from Employee ").executeUpdate();
            session.createMutationQuery("delete from Company").executeUpdate();
            transaction.commit();
        }
    }

    @Test
    public void markTaxAsPaid_success() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            MonthlyApartmentTaxDao.markTaxAsPaid(session, 1, LocalDate.now());
            transaction.commit();
        }

        assertTrue(MonthlyApartmentTaxDao.findMonthlyApartmentTaxPaymentStatus(1));
    }

    @Test
    public void markTaxAsPaid_nullTax_throwsException() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            assertThrows(
                    NotFoundException.class,
                    () -> MonthlyApartmentTaxDao.markTaxAsPaid(session, 4L, LocalDate.now())
            );
            transaction.commit();
        }
    }

    @Test
    public void markTaxAsPaid_deletedTax_throwsException() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
                MonthlyApartmentTaxDao.deleteMonthlyApartmentTax(session, 1L);
            transaction.commit();

            transaction = session.beginTransaction();
            assertThrows(
                    NotFoundException.class,
                    () -> MonthlyApartmentTaxDao.markTaxAsPaid(session, 1L, LocalDate.now())
            );
            transaction.commit();
        }
    }

    @Test
    public void findMonthlyApartmentTaxDtoById_success() {
        //1 occupant * 10 + 1 pet * 10 + 100m2 * 0.01 from apartment a1
        MonthlyApartmentTaxDto dto = MonthlyApartmentTaxDao.findMonthlyApartmentTaxDtoById(1L);

        assertEquals(0, dto.getPaymentValue().compareTo(BigDecimal.valueOf(21)));
    }

    @Test
    public void findMonthlyApartmentTaxDtoById_nullTax_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> MonthlyApartmentTaxDao.findMonthlyApartmentTaxDtoById(5L)
        );
    }

    @Test
    public void findMonthlyApartmentTaxDtoById_deletedTax_throwsException() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            MonthlyApartmentTaxDao.deleteMonthlyApartmentTax(session, 1L);
            transaction.commit();
        }

        assertThrows(
                NotFoundException.class,
                () -> MonthlyApartmentTaxDao.findMonthlyApartmentTaxDtoById(1L)
        );
    }

    @Test
    public void deleteMonthlyApartmentTax_success() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            MonthlyApartmentTaxDao.deleteMonthlyApartmentTax(session, 1L);
            transaction.commit();
        }

        assertThrows(
                NotFoundException.class,
                () -> MonthlyApartmentTaxDao.findMonthlyApartmentTaxDtoById(1L)
        );
    }

    @Test
    public void restoreMonthlyApartmentTax_success() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            MonthlyApartmentTaxDao.deleteMonthlyApartmentTax(session, 1L);
            transaction.commit();

            transaction = session.beginTransaction();
            MonthlyApartmentTaxDao.restoreMonthlyApartmentTax(session, 1L);
            transaction.commit();
        }

        MonthlyApartmentTaxDto dto = MonthlyApartmentTaxDao.findMonthlyApartmentTaxDtoById(1L);

        assertEquals(0, dto.getPaymentValue().compareTo(BigDecimal.valueOf(21)));
    }

    @Test
    public void findMonthlyApartmentTaxPaymentStatus_success() {
        assertFalse(MonthlyApartmentTaxDao.findMonthlyApartmentTaxPaymentStatus(1L));
    }

    @Test
    public void findMonthlyApartmentTaxPaymentStatus_nullTax_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> MonthlyApartmentTaxDao.findMonthlyApartmentTaxPaymentStatus(5L)
        );
    }

    @Test
    public void findMonthlyApartmentTaxPaymentStatus_deletedTax_throwsException() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            MonthlyApartmentTaxDao.deleteMonthlyApartmentTax(session, 1L);
            transaction.commit();
        }

        assertThrows(
                NotFoundException.class,
                () -> MonthlyApartmentTaxDao.findMonthlyApartmentTaxPaymentStatus(1L)
        );
    }
}
