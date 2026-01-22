package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.company.CompanyDto;
import org.cscb525.dto.company.CompanyIncomeDto;
import org.cscb525.dto.company.CreateCompanyDto;
import org.cscb525.dto.company.UpdateCompanyDto;
import org.cscb525.entity.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class CompanyDao {
    public static void createCompany(@Valid CreateCompanyDto createCompanyDto) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Company company = new Company();
            company.setName(createCompanyDto.getName());
            Transaction transaction = session.beginTransaction();
            session.persist(company);
            transaction.commit();
        }
    }

    public static void updateCompany(@Valid UpdateCompanyDto companyDto) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company = session.get(Company.class, companyDto.getId());
            if (company == null) {
                throw new EntityNotFoundException("No company with id " + companyDto.getId() + " found");
            }

            company.setName(companyDto.getName());
            transaction.commit();
        }
    }

    public static CompanyDto findCompanyById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CompanyDto> cr = cb.createQuery(CompanyDto.class);
            Root<Company> root = cr.from(Company.class);

            cr.select(cb.construct(
                    CompanyDto.class,
                    root.get("name")
            ))
                    .where(cb.equal(root.get("id"), id));
            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No company with id " + id + " found.");
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

    public static List<CompanyIncomeDto> getAllCompaniesOrderByIncomeDesc() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CompanyIncomeDto> cr = cb.createQuery(CompanyIncomeDto.class);
            Root<Company> root = cr.from(Company.class);

            Join<?, ?> employee = root.join("employees");
            Join<?, ?> building = employee.join("buildings");
            Join<?, ?> apartment = building.join("apartments");
            Join<?, ?> monthlyApartmentTax = apartment.join("monthlyApartmentTaxes");

            Expression<BigDecimal> income =
                    cb.coalesce(cb.sum(monthlyApartmentTax.get("paymentValue")), BigDecimal.ZERO);

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



}
