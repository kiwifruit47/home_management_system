package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeeDao {
    //employee can't be persisted if the company doesn't exist in the DB
    public static void createEmployee(@Valid Employee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company = session.get(
                    Company.class,
                    employee.getCompany().getId()
            );

            if (company == null) {
                throw new EntityNotFoundException(
                        "Company with id " + employee.getCompany().getId() + " does not exist."
                );
            }
            session.persist(employee);
            transaction.commit();
        }
    }

    public static void updateEmployee(@Valid Employee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company = session.get(
                    Company.class,
                    employee.getCompany().getId()
            );

            if (company == null) {
                throw new EntityNotFoundException(
                        "Company with id " + employee.getCompany().getId() + " does not exist."
                );
            }
            session.merge(employee);
            transaction.commit();
        }
    }

    public static Employee getEmployeeById(long id) {
        Employee employee;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            employee = session.get(Employee.class, id);
        }
        if (employee == null)
            throw new EntityNotFoundException("Employee with id " + id + " not found.");
        return employee;
    }

    public static List<Employee> getAllEmployees() {
        List<Employee> employees;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            employees = session.createQuery("select e from Employee e", Employee.class)
                    .getResultList();
        }
        return employees;
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
}
