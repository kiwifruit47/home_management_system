package dao.building;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.BuildingDao;
import org.cscb525.dto.building.BuildingDto;
import org.cscb525.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultipleBuildingIT {

    private long companyId;
    private long employeeId;

    @BeforeEach
    void setUp() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company company = new Company("Company");
            session.persist(company);
            companyId = company.getId();

            Employee employee = new Employee("Ivan", company);
            session.persist(employee);
            employeeId = employee.getId();

            Building building1 = new Building(
                    "ul. nqkoq 100",
                    5,
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.1),
                    employee
            );

            Building building2 = new Building(
                    "ul. nqkoq 101",
                    8,
                    BigDecimal.valueOf(12),
                    BigDecimal.valueOf(12),
                    BigDecimal.valueOf(0.2),
                    employee
            );

            session.persist(building1);
            session.persist(building2);

            tx.commit();
        }
    }

    @AfterEach
    void cleanDB() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Building").executeUpdate();
            session.createMutationQuery("delete from Employee").executeUpdate();
            session.createMutationQuery("delete from Company").executeUpdate();
            tx.commit();
        }
    }

    @Test
    void findAllBuildings_success() {
        List<BuildingDto> buildings = BuildingDao.findAllBuildings();

        assertEquals(2, buildings.size());
        assertTrue(
                buildings.stream().anyMatch(b -> b.getAddress().equals("ul. nqkoq 100"))
        );
        assertTrue(
                buildings.stream().anyMatch(b -> b.getAddress().equals("ul. nqkoq 101"))
        );
    }

    @Test
    void findAllBuildingCountByCompany_success() {
        long count = BuildingDao.findAllBuildingCountByCompany(companyId);
        assertEquals(2, count);
    }

    @Test
    void findAllBuildingsByCompany_success() {
        List<BuildingDto> buildings =
                BuildingDao.findAllBuildingsByCompany(companyId);

        assertEquals(2, buildings.size());
    }

    @Test
    void findAllBuildingsByEmployee_success() {
        List<BuildingDto> buildings =
                BuildingDao.findAllBuildingsByEmployee(employeeId);

        assertEquals(2, buildings.size());
    }

    @Test
    void deleteAllBuildingsByCompany_success() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            BuildingDao.deleteAllBuildingsByCompany(session, companyId);

            tx.commit();
        }

        List<BuildingDto> buildings = BuildingDao.findAllBuildings();
        assertEquals(0, buildings.size());
    }

    @Test
    void restoreAllBuildingsByCompany_success() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            BuildingDao.deleteAllBuildingsByCompany(session, companyId);
            tx.commit();
        }

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            BuildingDao.restoreAllBuildingsByCompany(session, companyId);
            tx.commit();
        }

        List<BuildingDto> buildings = BuildingDao.findAllBuildings();
        assertEquals(2, buildings.size());
    }

    @Test
    void findAllBuildings_deletedNotReturned() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Building building = session
                    .createQuery("from Building", Building.class)
                    .setMaxResults(1)
                    .getSingleResult();
            building.setDeleted(true);

            tx.commit();
        }

        List<BuildingDto> buildings = BuildingDao.findAllBuildings();
        assertEquals(1, buildings.size());
    }
}
