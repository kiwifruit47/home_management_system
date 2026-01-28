package dao.employee;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.EmployeeDao;
import org.cscb525.dto.employee.CreateEmployeeDto;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.cscb525.exceptions.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeCreateIT {
    @BeforeEach
    void setUp() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company company = new Company("Company");
            session.persist(company);

            tx.commit();
        }
    }

    @AfterEach
    void cleanDB() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Employee").executeUpdate();
            session.createMutationQuery("delete from Company").executeUpdate();
            tx.commit();
        }
    }

    @Test
    public void createEmployee_success() {
        long companyId;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            companyId = session.createQuery("select c.id from Company c", Long.class)
                    .getSingleResult();
        }

        CreateEmployeeDto dto = new CreateEmployeeDto("Gosho", companyId);

        EmployeeDao.createEmployee(dto);

        Employee employee;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            employee = session.createQuery("from Employee ", Employee.class)
                    .getSingleResult();
        }

        assertEquals("Gosho", employee.getName());
        assertEquals(companyId, employee.getCompany().getId());
    }

    @Test
    public void createEmployee_companyNotFound_throwsException() {
        CreateEmployeeDto dto = new CreateEmployeeDto("Gosho", 5L);

        assertThrows(
                NotFoundException.class,
                () -> EmployeeDao.createEmployee(dto)
        );
    }

    @Test
    void createEmployee_deletedCompany_throwsException() {
        Company company;
        long companyId;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            company = session.createQuery("from Company c", Company.class)
                    .getSingleResult();
            companyId = company.getId();
            Transaction transaction = session.beginTransaction();
            company.setDeleted(true);
            transaction.commit();
        }

        CreateEmployeeDto dto = new CreateEmployeeDto("Gosho", companyId);

        assertThrows(
                NotFoundException.class,
                () -> EmployeeDao.createEmployee(dto)
        );
    }
}
