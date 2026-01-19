package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.building.BuildingDto;
import org.cscb525.dto.company.CompanyIncomeDto;
import org.cscb525.dto.employee.EmployeeBuildingCountDto;
import org.cscb525.dto.employee.EmployeeDto;
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

    public static  List<BuildingDto> getAllBuildingsByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BuildingDto> cr = cb.createQuery(BuildingDto.class);
            Root<Building> root = cr.from(Building.class);

            Join<?, ?> employee = root.join("employee");
            Join<?, ?> company = employee.join("company");

            cr.select(cb.construct(
                    BuildingDto.class,
                    root.get("address")
            ))
                    .where(cb.equal(company.get("id"), companyId));
            return session.createQuery(cr).getResultList();
        }
    }

    public static long getAllBuildingCountByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<Building> root = cr.from(Building.class);

            Join<?, ?> company = root.join("company");

            cr.select(cb.construct(
                    Long.class,
                    cb.count(root)
            ))
                    .where(cb.equal(company.get("id"), companyId));

            return session.createQuery(cr).getSingleResult();
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
