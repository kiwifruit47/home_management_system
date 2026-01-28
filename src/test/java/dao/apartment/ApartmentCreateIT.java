package dao.apartment;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.ApartmentDao;
import org.cscb525.dto.apartment.CreateApartmentDto;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.Building;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.cscb525.exceptions.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ApartmentCreateIT {
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

            tx.commit();
        }
    }

    @AfterEach
    void cleanDB() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Apartment").executeUpdate();
            session.createMutationQuery("delete from Building").executeUpdate();
            session.createMutationQuery("delete from Employee").executeUpdate();
            session.createMutationQuery("delete from Company").executeUpdate();
            tx.commit();
        }
    }

    @Test
    void createApartment_Success() {
        Long buildingId;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            buildingId = session
                    .createQuery("select b.id from Building b", Long.class)
                    .getSingleResult();
        }

        CreateApartmentDto dto = new CreateApartmentDto(
                1,
                1,
                BigDecimal.valueOf(100),
                0,
                buildingId
        );

        ApartmentDao.createApartment(dto);

        Apartment apartment;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartment = session
                    .createQuery("from Apartment", Apartment.class)
                    .getSingleResult();
        }

        assertEquals(1, apartment.getApartmentNumber());
        assertEquals(0, BigDecimal.valueOf(100).compareTo(apartment.getArea()));
        assertEquals(1, apartment.getFloor());
        assertEquals(0, apartment.getPets());
        assertNotNull(apartment.getBuilding());
    }

    @Test
    void createApartment_buildingNotFound_throwsException() {
        CreateApartmentDto dto = new CreateApartmentDto(
                1,
                1,
                BigDecimal.valueOf(100),
                0,
                5L
        );
        assertThrows(
                NotFoundException.class,
                () -> ApartmentDao.createApartment(dto)
        );
    }

    @Test
    void createApartment_deletedBuilding_throwsException() {
        Building building;
        long buildingId;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            building = session.createQuery("from Building b", Building.class)
                    .getSingleResult();
            buildingId = building.getId();
            Transaction transaction = session.beginTransaction();
            building.setDeleted(true);
            transaction.commit();
        }

        CreateApartmentDto dto = new CreateApartmentDto(
                1,
                1,
                BigDecimal.valueOf(100),
                0,
                buildingId
        );

        assertThrows(
                NotFoundException.class,
                () -> ApartmentDao.createApartment(dto)
        );
    }

}

