package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.entity.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;

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


}
