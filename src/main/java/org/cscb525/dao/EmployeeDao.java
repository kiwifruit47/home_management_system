package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.employee.CreateEmployeeDto;
import org.cscb525.dto.employee.EmployeeBuildingCountDto;
import org.cscb525.dto.employee.EmployeeDto;
import org.cscb525.dto.employee.UpdateEmployeeDto;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeeDao {
    //employee can't be persisted if the company doesn't exist in the DB
    public static void createEmployee(@Valid CreateEmployeeDto createEmployeeDto) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Company company = session.get(Company.class, createEmployeeDto.getCompanyId());

            if (company == null) {
                throw new EntityNotFoundException("No company with id " + createEmployeeDto.getCompanyId() + " found.");
            }

            Employee employee = new Employee();
            employee.setName(createEmployeeDto.getName());
            employee.setCompany(company);

            session.persist(employee);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static void updateEmployee(@Valid UpdateEmployeeDto employeeDto) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company = session.get(
                    Company.class,
                    employeeDto.getCompanyId()
            );

            Employee employee = session.get(
                    Employee.class,
                    employeeDto.getEmployeeId()
            );

            if (employee == null) {
                throw new EntityNotFoundException("No employee with id " + employeeDto.getEmployeeId() + " found.");
            }

            if (company == null || !company.equals(employee.getCompany())) {
                throw new EntityNotFoundException(
                        "Employee with id " + employeeDto.getEmployeeId() +
                        " does not belong to company with id " + employeeDto.getCompanyId() + "."
                );
            }

            employee.setName(employeeDto.getName());
            transaction.commit();
        }
    }

    public static EmployeeDto findEmployeeById(long id) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<EmployeeDto> cr = cb.createQuery(EmployeeDto.class);
            Root<Employee> root = cr.from(Employee.class);

            cr.select(cb.construct(
                            EmployeeDto.class,
                            root.get("name")
                    ))
                    .where(cb.equal(root.get("id"), id));
            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No employee with id " + id + " found.");
        }
    }

    public static List<EmployeeDto> findAllEmployees() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<EmployeeDto> cr = cb.createQuery(EmployeeDto.class);
            Root<Employee> root = cr.from(Employee.class);

            cr.select(cb.construct(
                            EmployeeDto.class,
                            root.get("name")
                    ))
                    .where(cb.isFalse(root.get("deleted")));
            return session.createQuery(cr).getResultList();
        }
    }
    public static void deleteEmployee(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Employee e set e.deleted = true where e.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Employee with id " + id + " not found.");
            }
            transaction.commit();
        }
    }
    public static void restoreEmployee(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Employee e set e.deleted = false where e.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Employee with id " + id + " not found.");
            }
            transaction.commit();
        }
    }

    public static List<EmployeeDto> getAllEmployeesByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<EmployeeDto> cr = cb.createQuery(EmployeeDto.class);
            Root<Employee> root = cr.from(Employee.class);

            Join<?, ?> company = root.join("company");

            cr.select(cb.construct(
                            EmployeeDto.class,
                            root.get("name")
                    ))
                    .where(cb.equal(company.get("id"), companyId));

            return session.createQuery(cr).getResultList();
        }
    }

    public static List<EmployeeBuildingCountDto> getEmployeesGroupByCompanyOrderByBuildingCountDescAndNameAsc(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<EmployeeBuildingCountDto> cr = cb.createQuery(EmployeeBuildingCountDto.class);
            Root<Company> root = cr.from(Company.class);

            Join<?, ?> employee = root.join("employees");
            Join<?, ?> building = employee.join("buildings");

            Expression<Long> buildingCount = cb.count(building);

            cr.select(cb.construct(
                            EmployeeBuildingCountDto.class,
                            employee.get("name"),
                            cb.count(building)
                    ))
                    .where(cb.equal(root.get("id"), companyId))
                    .groupBy(employee.get("name"))
                    .orderBy(
                            cb.desc(buildingCount),
                            cb.asc(employee.get("name")
                            ));

            return session.createQuery(cr).getResultList();
        }
    }
}
