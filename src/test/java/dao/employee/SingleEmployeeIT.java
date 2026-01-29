package dao.employee;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.EmployeeDao;
import org.cscb525.dto.employee.EmployeeDto;
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

public class SingleEmployeeIT {
    @BeforeEach
    void setup() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = new Company("Company");
            session.persist(company);

            Employee employee = new Employee("Sasho", company);
            session.persist(employee);

            transaction.commit();
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

    private long getEmployeeId() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("select e.id from Employee e", Long.class)
                    .getSingleResult();
        }
    }

    @Test
    public void updateEmployeeName_success() {
        EmployeeDao.updateEmployeeName(getEmployeeId(), "Gosho");
        EmployeeDto employee = EmployeeDao.findEmployeeDtoById(getEmployeeId());

        assertEquals("Gosho", employee.getName());
    }

    @Test
    public void updateCEmployeeName_nullEmployee_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> EmployeeDao.updateEmployeeName(5L, "Gosho")
        );
    }

    @Test
    public void updateEmployeeName_deletedEmployee_throwsException() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            EmployeeDao.deleteEmployee(session, getEmployeeId());
            transaction.commit();
        }

        assertThrows(
                NotFoundException.class,
                () -> EmployeeDao.updateEmployeeName(getEmployeeId(), "Gosho")
        );
    }

    @Test
    public void findEmployeeDtoById_success() {
        EmployeeDto employee = EmployeeDao.findEmployeeDtoById(getEmployeeId());

        assertEquals("Sasho", employee.getName());
    }

    @Test
    public void findEmployeeDtoById_nullEmployee_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> EmployeeDao.findEmployeeDtoById(5L)
        );
    }

    @Test
    public void findEmployeeDtoById_deletedCompany_throwsException() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            EmployeeDao.deleteEmployee(session, getEmployeeId());
            transaction.commit();
        }

        assertThrows(
                NotFoundException.class,
                () -> EmployeeDao.findEmployeeDtoById(getEmployeeId())
        );
    }
}
