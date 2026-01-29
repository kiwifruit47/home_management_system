package dao.company;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.CompanyDao;
import org.cscb525.dto.company.CompanyDto;
import org.cscb525.entity.Company;
import org.cscb525.exceptions.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SingleCompanyIT {
    @BeforeEach
    void setup() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company = new Company("Company");
            session.persist(company);
            transaction.commit();
        }
    }

    @AfterEach
    void cleanDB() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Company").executeUpdate();
            tx.commit();
        }
    }

    private long getCompanyId() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("select c.id from Company c", Long.class)
                    .getSingleResult();
        }
    }

    @Test
    public void updateCompanyName_success() {
        CompanyDao.updateCompanyName(getCompanyId(), "Updated Company");
        CompanyDto company = CompanyDao.findCompanyDtoById(getCompanyId());

        assertEquals("Updated Company", company.getName());
    }

    @Test
    public void updateCompanyName_nullCompany_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> CompanyDao.updateCompanyName(5L, "Updated Company")
        );
    }

    @Test
    public void updateCompanyName_deletedCompany_throwsException() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            CompanyDao.deleteCompany(session, getCompanyId());
            transaction.commit();
        }

        assertThrows(
                NotFoundException.class,
                () -> CompanyDao.updateCompanyName(5L, "Updated Company")
        );
    }

    @Test
    public void findCompanyDtoById_success() {
        CompanyDto company = CompanyDao.findCompanyDtoById(getCompanyId());

        assertEquals("Company", company.getName());
    }

    @Test
    public void findCompanyDtoById_nullCompany_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> CompanyDao.findCompanyDtoById(5L)
        );
    }

    @Test
    public void findCompanyDtoById_deletedCompany_throwsException() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            CompanyDao.deleteCompany(session, getCompanyId());
            transaction.commit();
        }

        assertThrows(
                NotFoundException.class,
                () -> CompanyDao.findCompanyDtoById(getCompanyId())
        );
    }

    @Test
    public void deleteCompany_success() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            CompanyDao.deleteCompany(session, getCompanyId());
            transaction.commit();
        }

        assertThrows(
                NotFoundException.class,
                () -> CompanyDao.findCompanyDtoById(getCompanyId())
        );
    }

    @Test
    public void restoreCompany_success() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            CompanyDao.deleteCompany(session, getCompanyId());
            transaction.commit();
        }

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            CompanyDao.restoreCompany(session, getCompanyId());
            transaction.commit();
        }

        CompanyDto company = CompanyDao.findCompanyDtoById(getCompanyId());

        assertEquals("Company", company.getName());
    }


}
