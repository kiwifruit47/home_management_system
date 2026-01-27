package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.company.CompanyDto;
import org.cscb525.dto.company.CompanyIncomeDto;
import org.cscb525.dto.company.CreateCompanyDto;
import org.cscb525.entity.*;
import org.cscb525.exceptions.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class CompanyDao {
    public static void createCompany(CreateCompanyDto createCompanyDto) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Company company = new Company();
            company.setName(createCompanyDto.getName());
            Transaction transaction = session.beginTransaction();
            session.persist(company);
            transaction.commit();
        }
    }

    public static void updateCompanyName(long companyId, String name) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company = session.get(Company.class, companyId);
            if (company == null || company.isDeleted()) {
                throw new EntityNotFoundException("No active company with id " + companyId + " found");
            }

            company.setName(name);
            transaction.commit();
        }
    }

    public static CompanyDto findCompanyDtoById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CompanyDto> cr = cb.createQuery(CompanyDto.class);
            Root<Company> root = cr.from(Company.class);

            cr.select(cb.construct(
                    CompanyDto.class,
                    root.get("name")
            ))
                    .where(cb.and(
                            cb.equal(root.get("id"), id),
                            cb.isFalse(root.get("deleted"))
                    ));
            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException(Company.class, id, e);
        }
    }

    public static CompanyDto findCompanyDtoById(Session session, long id) {
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CompanyDto> cr = cb.createQuery(CompanyDto.class);
            Root<Company> root = cr.from(Company.class);

            cr.select(cb.construct(
                            CompanyDto.class,
                            root.get("name")
                    ))
                    .where(cb.and(
                            cb.equal(root.get("id"), id),
                            cb.isFalse(root.get("deleted"))
                    ));
            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException(Company.class, id, e);
        }
    }

    public static List<CompanyDto> findAllCompanies() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CompanyDto> cr = cb.createQuery(CompanyDto.class);
            Root<Company> root = cr.from(Company.class);

            cr.select(cb.construct(
                    CompanyDto.class,
                    root.get("name")
            ))
                    .where(cb.isFalse(root.get("deleted")));
            return session.createQuery(cr).getResultList();
        }
    }

    public static void deleteCompany(Session session, long id) {
        session.createQuery("update Company c set c.deleted = true where c.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public static void restoreCompany(Session session, long id) {
        session.createQuery("update Company c set c.deleted = false where c.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public static List<CompanyIncomeDto> companiesOrderByIncomeDesc() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CompanyIncomeDto> cr = cb.createQuery(CompanyIncomeDto.class);
            Root<Company> root = cr.from(Company.class);

            Join<Company, Employee> employee = root.join("employees", JoinType.LEFT);
            Join<Employee, Building> building = employee.join("buildings", JoinType.LEFT);
            Join<Building, Apartment> apartment = building.join("apartments", JoinType.LEFT);
            Join<Apartment, MonthlyApartmentTax> monthlyApartmentTax = apartment.join("monthlyApartmentTaxes", JoinType.LEFT);

            Expression<BigDecimal> income =
                    cb.coalesce(cb.sum(monthlyApartmentTax.get("paymentValue")), BigDecimal.ZERO);

            cr.select(cb.construct(
                    CompanyIncomeDto.class,
                    root.get("name"),
                    income
            ))
                    .where(cb.isFalse(root.get("deleted")))
                    .groupBy(root.get("id"),root.get("name"))
                    .orderBy(cb.desc(income));

            return session.createQuery(cr).getResultList();
        }
    }



}
