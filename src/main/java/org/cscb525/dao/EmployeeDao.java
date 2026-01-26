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
import org.cscb525.entity.Building;
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

            if (employee == null || employee.isDeleted()) {
                throw new EntityNotFoundException("No active employee with id " + employeeDto.getEmployeeId() + " found.");
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
                    .where(cb.and(
                            cb.equal(root.get("id"), id),
                            cb.isFalse(root.get("deleted"))
                    ));
            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No active employee with id " + id + " found.");
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
    public static void deleteEmployee(Session session, long id) {
        int updatedRows = session.createQuery("update Employee e set e.deleted = true where e.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public static void deleteAllEmployeesByCompany(Session session, long companyId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Employee> update = cb.createCriteriaUpdate(Employee.class);
        Root<Employee> root = update.from(Employee.class);

        update.set(root.get("deleted"), true)
                .where(
                        cb.and(
                                cb.equal(root.get("company").get("id"), companyId),
                                cb.isFalse(root.get("deleted"))
                        )
                );

        session.createMutationQuery(update).executeUpdate();
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

    public static List<EmployeeDto> findAllEmployeesByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<EmployeeDto> cr = cb.createQuery(EmployeeDto.class);
            Root<Employee> root = cr.from(Employee.class);

            Join<Employee, Company> company = root.join("company");

            cr.select(cb.construct(
                            EmployeeDto.class,
                            root.get("name")
                    ))
                    .where(cb.and(
                            cb.equal(company.get("id"), companyId),
                            cb.isFalse(root.get("deleted"))
                    ));

            return session.createQuery(cr).getResultList();
        }
    }

    public static List<EmployeeBuildingCountDto> findEmployeesGroupByCompanyOrderByBuildingCountDescAndNameAsc(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<EmployeeBuildingCountDto> cr = cb.createQuery(EmployeeBuildingCountDto.class);
            Root<Employee> root = cr.from(Employee.class);

            Join<Employee, Company> company = root.join("company");
            Join<Employee, Building> building =
                    root.join("buildings", JoinType.LEFT);

            cr.select(cb.construct(
                            EmployeeBuildingCountDto.class,
                            root.get("name"),
                            cb.count(building)
                    ))
                    .where(cb.and(
                            cb.equal(root.get("id"), companyId),
                            cb.isFalse(root.get("deleted"))
                    ))
                    .groupBy(root.get("id"), root.get("name"))
                    .orderBy(
                            cb.desc(cb.count(building)),
                            cb.asc(root.get("name")
                            ));

            return session.createQuery(cr).getResultList();
        }
    }

    public static Long findEmployeeWithSmallestBuildingCountByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<Employee> root = cr.from(Employee.class);

            Join<Employee, Company> company = root.join("company");
            Join<Employee, Building> building =
                    root.join("buildings", JoinType.LEFT);

            cr.select(root.get("id"))
                    .where(cb.and(
                            cb.equal(company.get("id"), companyId),
                            cb.isFalse(root.get("deleted"))
                    ))
                    .groupBy(root.get("id"))
                    .orderBy(cb.asc(cb.count(building)));

            return session.createQuery(cr)
                    .setMaxResults(1)
                    .getSingleResult();
        }
    }

    public static List<Long> findAllColleaguesIdsOfEmployee(Session session, long companyId, long employeeId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cr = cb.createQuery(Long.class);
        Root<Employee> root = cr.from(Employee.class);

        Join<Employee, Company> company = root.join("company");

        cr.select(root.get("id"))
                .where(cb.and(
                        cb.equal(company.get("id"), companyId),
                        cb.notEqual(root.get("id"), employeeId),
                        cb.isFalse(root.get("deleted"))
                ));
        return session.createQuery(cr).getResultList();
    }

}
