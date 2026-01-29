package dao.employee;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.EmployeeDao;
import org.cscb525.dto.employee.EmployeeBuildingCountDto;
import org.cscb525.dto.employee.EmployeeDto;
import org.cscb525.entity.Building;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MultipleEmployeeIT {
    @BeforeEach
    void setup() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = new Company("Company");
            session.persist(company);

            Employee e1 = new Employee("Sasho", company);
            session.persist(e1);

            Employee e2 = new Employee("Pesho", company);
            session.persist(e2);

            Employee e3 = new Employee("Gosho", company);
            session.persist(e3);

            Building b1 = new Building(
                    "ul. nqkoq 100",
                    5,
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.1),
                    e1
            );
            session.persist(b1);

            Building b2 = new Building(
                    "ul. nqkoq 101",
                    8,
                    BigDecimal.valueOf(12),
                    BigDecimal.valueOf(12),
                    BigDecimal.valueOf(0.2),
                    e1
            );
            session.persist(b2);

            Building b3 = new Building(
                    "ul. nqkoq 102",
                    5,
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.1),
                    e1
            );
            session.persist(b3);

            Building b4 = new Building(
                    "ul. nqkoq 103",
                    8,
                    BigDecimal.valueOf(12),
                    BigDecimal.valueOf(12),
                    BigDecimal.valueOf(0.2),
                    e2
            );
            session.persist(b4);

            Building b5 = new Building(
                    "ul. nqkoq 104",
                    5,
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.1),
                    e2
            );
            session.persist(b5);

            Building b6 = new Building(
                    "ul. nqkoq 105",
                    8,
                    BigDecimal.valueOf(12),
                    BigDecimal.valueOf(12),
                    BigDecimal.valueOf(0.2),
                    e3
            );
            session.persist(b6);

            transaction.commit();
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
    public void findAllEmployees_success() {
        List<EmployeeDto> employees = EmployeeDao.findAllEmployees();

        assertEquals(3, employees.size());
        assertTrue(
                employees.stream().anyMatch(e -> e.getName().equals("Sasho"))
        );
        assertTrue(
                employees.stream().anyMatch(e -> e.getName().equals("Pesho"))
        );
        assertTrue(
                employees.stream().anyMatch(e -> e.getName().equals("Gosho"))
        );
    }

    @Test
    public void deleteAllEmployeesByCompany_success() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            EmployeeDao.deleteAllEmployeesByCompany(session, 1);
            transaction.commit();
        }

        List<EmployeeDto> employees = EmployeeDao.findAllEmployees();

        assertEquals(0, employees.size());
    }

    @Test
    public void restoreAllEmployeesByCompany_success() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            EmployeeDao.deleteAllEmployeesByCompany(session, 1);
            transaction.commit();
        }

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            EmployeeDao.restoreAllEmployeesByCompany(session, 1);
            transaction.commit();
        }

        List<EmployeeDto> employees = EmployeeDao.findAllEmployees();

        assertEquals(3, employees.size());

    }

    @Test
    public void findAllEmployeesByCompany_success() {
        assertEquals(3, EmployeeDao.findAllEmployeesByCompany(1).size());
    }

    @Test
    public void findAllEmployeesByCompany_returnsZeroResultsWhenCompanyNotFound() {
        assertEquals(0, EmployeeDao.findAllEmployeesByCompany(2).size());
    }

    @Test
    public void findEmployeesGroupByCompanyOrderByBuildingCountDescAndNameAsc_success() {
        List<EmployeeBuildingCountDto> employees =
                EmployeeDao.findEmployeesGroupByCompanyOrderByBuildingCountDescAndNameAsc(1);

        assertEquals(3, employees.size());

        long comparator = employees.getFirst().getBuildings() + 1;
        for (EmployeeBuildingCountDto employee: employees) {
            assertTrue(comparator > employee.getBuildings());
            comparator = employee.getBuildings();
        }
    }

    @Test
    public void findEmployeeIdWithSmallestBuildingCountByCompany_success() {
        assertEquals(3L, EmployeeDao.findEmployeeIdWithSmallestBuildingCountByCompany(1));
    }

    @Test
    public void findAllColleaguesIdsOfEmployee_success() {
        List<Long> ids;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            ids = EmployeeDao.findAllColleaguesIdsOfEmployee(session, 1L, 1L);

        }
        List<Long> compareIds = new ArrayList<>();
        compareIds.add(2L);
        compareIds.add(3L);

        assertEquals(compareIds.toString(), ids.toString());
    }

}
