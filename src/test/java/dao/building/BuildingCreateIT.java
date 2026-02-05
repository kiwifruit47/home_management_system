package dao.building;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.BuildingDao;
import org.cscb525.dto.building.CreateBuildingDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BuildingCreateIT {
    @BeforeEach
    void setUp() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company company = new Company("Company");
            session.persist(company);

            Employee employee = new Employee("Gosho", company);
            session.persist(employee);

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
    public void createBuilding_success() {
        long employeeId;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            employeeId = session.createQuery("select e.id from Employee e", Long.class)
                    .getSingleResult();
        }

        CreateBuildingDto dto = new CreateBuildingDto(
                "ul. nqkoq 100",
                8,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(0.01),
                employeeId
        );
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            BuildingDao.createBuilding(session, dto);
            transaction.commit();
        }


        Building building;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            building = session.createQuery("from Building ", Building.class)
                    .getSingleResult();
        }

        assertEquals("ul. nqkoq 100", building.getAddress());
        assertEquals(8, building.getFloors());
        assertEquals(0, BigDecimal.valueOf(10).compareTo(building.getMonthlyTaxPerPerson()));
        assertEquals(0, BigDecimal.valueOf(10).compareTo(building.getMonthlyTaxPerPet()));
        assertEquals(0, BigDecimal.valueOf(0.01).compareTo(building.getMonthlyTaxPerM2()));
        assertEquals(employeeId, building.getEmployee().getId());
    }

    @Test
    public void createBuilding_employeeNotFound_throwsException() {
        CreateBuildingDto dto = new CreateBuildingDto(
                "ul. nqkoq 100",
                8,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(0.01),
                5L
        );

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            assertThrows(
                    NotFoundException.class,
                    () -> BuildingDao.createBuilding(session, dto)
            );
            transaction.commit();
        }
    }

    @Test
    void createBuilding_deletedEmployee_throwsException() {
        Employee employee;
        long employeeId;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            employee = session.createQuery("from Employee e", Employee.class)
                    .getSingleResult();
            employeeId = employee.getId();
            Transaction transaction = session.beginTransaction();
            employee.setDeleted(true);
            transaction.commit();
        }

        CreateBuildingDto dto = new CreateBuildingDto(
                "ul. nqkoq 100",
                8,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(0.01),
                employeeId
        );

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            assertThrows(
                    NotFoundException.class,
                    () -> BuildingDao.createBuilding(session, dto)
            );
            transaction.commit();
        }
    }
}
