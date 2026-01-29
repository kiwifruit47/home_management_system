package dao.building;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.BuildingDao;
import org.cscb525.dto.building.BuildingDto;
import org.cscb525.dto.building.UpdateBuildingDto;
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

public class SingleBuildingIT {
    @BeforeEach
    void setup() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company company = new Company("Company");
            session.persist(company);

            Employee employee = new Employee("Gosho", company);
            session.persist(employee);

            Employee employee2 = new Employee("Pesho", company);
            session.persist(employee2);

            Building building = new Building(
                    "ul. nqkoq 100",
                    10,
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
            session.createMutationQuery("delete from Building").executeUpdate();
            session.createMutationQuery("delete from Employee").executeUpdate();
            session.createMutationQuery("delete from Company").executeUpdate();
            tx.commit();
        }
    }

    private long findBuildingId() {
        long buildingId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            buildingId = session.createQuery("select b.id from Building b", Long.class)
                    .getSingleResult();
        }
        return buildingId;
    }

    @Test
    public void updateBuilding_success() {
        long buildingId = findBuildingId();

        UpdateBuildingDto dto = new UpdateBuildingDto(buildingId, BigDecimal.valueOf(12), BigDecimal.valueOf(12), BigDecimal.valueOf(0.02));

        BuildingDao.updateBuilding(dto);

        Building building;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            building = session.get(Building.class, buildingId);
        }

        assertEquals(0, BigDecimal.valueOf(12).compareTo(building.getMonthlyTaxPerPerson()));
        assertEquals(0, BigDecimal.valueOf(12).compareTo(building.getMonthlyTaxPerPet()));
        assertEquals(0, BigDecimal.valueOf(0.02).compareTo(building.getMonthlyTaxPerM2()));
    }

    @Test
    public void updateBuilding_buildingNotFound_throwsException() {
        UpdateBuildingDto dto = new UpdateBuildingDto(5L, BigDecimal.valueOf(12), BigDecimal.valueOf(12), BigDecimal.valueOf(0.02));

        assertThrows(
                NotFoundException.class,
                () -> BuildingDao.updateBuilding(dto)
        );
    }

    @Test
    public void updateBuilding_deletedBuilding_throwsException() {
        long buildingId = findBuildingId();
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            BuildingDao.deleteBuilding(session, buildingId);
            transaction.commit();
        }

        UpdateBuildingDto dto = new UpdateBuildingDto(buildingId, BigDecimal.valueOf(12), BigDecimal.valueOf(12), BigDecimal.valueOf(0.02));

        assertThrows(
                NotFoundException.class,
                () -> BuildingDao.updateBuilding(dto)
        );
    }

    @Test
    public void findBuildingDtoById_success() {
        long buildingId = findBuildingId();

        BuildingDto dto = BuildingDao.findBuildingDtoById(buildingId);

        assertEquals(0, BigDecimal.valueOf(10).compareTo(dto.getMonthlyTaxPerPerson()));
        assertEquals(0, BigDecimal.valueOf(10).compareTo(dto.getMonthlyTaxPerPet()));
        assertEquals(0, BigDecimal.valueOf(0.01).compareTo(dto.getMonthlyTaxPerM2()));
        assertEquals(10, dto.getFloors());
        assertEquals(buildingId, dto.getId());
    }

    @Test
    public void findBuildingDtoById_buildingNotFound_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> BuildingDao.findBuildingDtoById(5L)
        );
    }

    @Test
    public void deleteBuilding_success() {
        long buildingId = findBuildingId();
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            BuildingDao.deleteBuilding(session, buildingId);
            transaction.commit();
        }

        assertThrows(
                NotFoundException.class,
                () -> BuildingDao.findBuildingDtoById(buildingId)
        );
    }

    @Test
    public void updateEmployeeForBuilding_success() {
        long buildingId = findBuildingId();
        Building building;
        long employeeId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            employeeId = session.createQuery("select e.id from Employee e order by id desc", Long.class)
                    .setMaxResults(1)
                    .getSingleResult();

            Transaction transaction = session.beginTransaction();
            BuildingDao.updateEmployeeForBuilding(session, buildingId,employeeId);
            transaction.commit();

            building = session.get(Building.class, buildingId);
        }

        assertEquals(employeeId, building.getEmployee().getId());
    }
}
