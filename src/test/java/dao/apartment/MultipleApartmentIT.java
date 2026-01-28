package dao.apartment;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.ApartmentDao;
import org.cscb525.dto.apartment.ApartmentDto;
import org.cscb525.entity.*;
import org.cscb525.exceptions.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class MultipleApartmentIT {

    private long companyId;
    private long buildingId;

    @BeforeEach
    void setUp() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company company = new Company("Company");
            session.persist(company);
            companyId = company.getId();

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
            buildingId = building.getId();

            Apartment a1 = new Apartment(1, 1, BigDecimal.valueOf(50), 0, building);
            Apartment a2 = new Apartment(2, 2, BigDecimal.valueOf(75), 1, building);
            Apartment a3 = new Apartment(3, 3, BigDecimal.valueOf(100), 2, building);

            session.persist(a1);
            session.persist(a2);
            session.persist(a3);

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
    public void findAllApartments_success() {
        List<ApartmentDto> apartments = ApartmentDao.findAllApartments();

        assertEquals(3, apartments.size());
    }

    @Test
    public void findAllApartmentIds_success() {
        List<Long> ids = ApartmentDao.findAllApartmentIds();
        List<Long> matchingIds = List.of(1L, 2L, 3L);

        assertEquals(3, ids.size());
        assertEquals(matchingIds, ids);
    }

    @Test
    public void deleteAllApartmentsByCompany_success() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            ApartmentDao.deleteAllApartmentsByCompany(session, companyId);
            tx.commit();
        }

        List<Long> ids = ApartmentDao.findAllApartmentIds();
        assertEquals(0, ids.size());
    }

    @Test
    public void restoreAllApartmentsByCompany_success() {
        List<ApartmentDto> apartments;
        List<ApartmentDto> apartments2;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartments = ApartmentDao.findAllApartments();
            Transaction tx = session.beginTransaction();
            ApartmentDao.deleteAllApartmentsByCompany(session, companyId);
            tx.commit();
        }

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            ApartmentDao.restoreAllApartmentsByCompany(session, companyId);
            tx.commit();
            apartments2 = ApartmentDao.findAllApartments();
        }

        assertEquals(apartments.toString(), apartments2.toString());
    }

    @Test
    public void deleteAllApartmentsByBuilding_success() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            ApartmentDao.deleteAllApartmentsByBuilding(session, buildingId);
            tx.commit();
        }

        List<Long> ids = ApartmentDao.findAllApartmentIds();
        assertEquals(0, ids.size());
    }

    @Test
    public void findApartmentCountByBuilding_success() {
        long count = ApartmentDao.findApartmentCountByBuilding(buildingId);
        assertEquals(3L, count);
    }

    @Test
    public void findApartmentCountByBuilding_afterDelete_returnsZero() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            ApartmentDao.deleteAllApartmentsByBuilding(session, buildingId);
            tx.commit();
        }

        long count = ApartmentDao.findApartmentCountByBuilding(buildingId);
        assertEquals(0L, count);
    }

    @Test
    public void findApartmentCountByBuilding_buildingNotFound_returnsZero() {
        long buildingCount = ApartmentDao.findApartmentCountByBuilding(5L);
        assertEquals(0L, buildingCount);
    }

    @Test
    public void findAllApartmentsByBuilding_success() {
        List<ApartmentDto> result =
                ApartmentDao.findAllApartmentsByBuilding(buildingId);

        assertEquals(3, result.size());
    }

    @Test
    public void findAllApartmentsByBuilding_afterDelete_returnsEmpty() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            ApartmentDao.deleteAllApartmentsByBuilding(session, buildingId);
            tx.commit();
        }

        List<ApartmentDto> result =
                ApartmentDao.findAllApartmentsByBuilding(buildingId);

        assertEquals(0, result.size());
    }
}

