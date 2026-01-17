package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.company.CompanyIncomeDto;
import org.cscb525.dto.employee.EmployeeBuildingsDto;
import org.cscb525.entity.Building;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class CompanyDao {
    public static void createCompany(@Valid Company company) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(company);
            transaction.commit();
        }
    }

    public static void updateCompany(@Valid Company company) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(company);
            transaction.commit();
        }
    }

    public static Company findCompanyById(long id) {
        Company company;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            company = session.get(Company.class, id);
        }
        if (company == null)
            throw new EntityNotFoundException("No company with id " + id + " found.");
        return company;
    }

    public static List<Company> findAllCompanies() {
        List<Company> companies;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            companies = session.createQuery("select c from Company c", Company.class)
                    .getResultList();
        }
        return companies;
    }

    //throws exception if nothing is deleted (no company with such id found)
    public static void deleteCompany(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Company c set c.deleted = true where c.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("No company with id " + id + " found.");
            }
            transaction.commit();
        }
    }

    //throws exception if nothing is restored (no company with such id found)
    public static void restoreCompany(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Company c set c.deleted = false where c.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("No company with id " + id + " found.");
            }
            transaction.commit();
        }
    }

    public static List<Employee> getAllEmployeesByCompany(Company company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
            Root<Employee> root = cr.from(Employee.class);

            cr.select(root).where(cb.equal(root.get("company"), company));

            return session.createQuery(cr).getResultList();
        }
    }

    public static List<CompanyIncomeDto> getAllCompaniesOrderByIncomeDesc() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CompanyIncomeDto> cr = cb.createQuery(CompanyIncomeDto.class);
            Root<Company> root = cr.from(Company.class);

            Join<?, ?> employee = root.join("employees");
            Join<?, ?> building = employee.join("buildings");
            Join<?, ?> apartment = building.join("apartments");
            Join<?, ?> monthlyApartmentTax = apartment.join("monthly_apartment_taxes");

            Expression<BigDecimal> income =
                    cb.coalesce(cb.sum(monthlyApartmentTax.get("payment_value")), BigDecimal.ZERO);

            cr.select(cb.construct(
                    CompanyIncomeDto.class,
                    root.get("name"),
                    income
            ))
            .groupBy(root.get("name"))
                    .orderBy(cb.desc(income));

            return session.createQuery(cr).getResultList();
        }
    }

    public static List<EmployeeBuildingsDto> getEmployeesGroupByCompanyOrderByBuildingCountDescAndNameAsc(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<EmployeeBuildingsDto> cr = cb.createQuery(EmployeeBuildingsDto.class);
            Root<Company> root = cr.from(Company.class);

            Join<?, ?> employee = root.join("employees");
            Join<?, ?> building = employee.join("buildings");

            Expression<Long> buildingCount = cb.count(building);

            cr.select(cb.construct(
                    EmployeeBuildingsDto.class,
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
