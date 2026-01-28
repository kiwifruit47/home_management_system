package dao.company;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.BuildingDao;
import org.cscb525.dao.CompanyDao;
import org.cscb525.dto.building.CreateBuildingDto;
import org.cscb525.dto.company.CreateCompanyDto;
import org.cscb525.entity.Building;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.cscb525.exceptions.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompanyCreateIT {

    @AfterEach
    void cleanDB() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Company").executeUpdate();
            tx.commit();
        }
    }

    @Test
    public void createCompany_success() {
        CreateCompanyDto dto = new CreateCompanyDto("Company");

        CompanyDao.createCompany(dto);

        Company company;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            company = session.createQuery("from Company", Company.class)
                    .getSingleResult();
        }

        assertEquals("Company", company.getName());
    }
}
