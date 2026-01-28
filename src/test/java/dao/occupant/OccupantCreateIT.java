package dao.occupant;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.OccupantDao;
import org.cscb525.dto.occupant.CreateOccupantDto;
import org.cscb525.entity.*;
import org.cscb525.exceptions.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OccupantCreateIT {
    @BeforeEach
    void setUp() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Company company = new Company("Company");
            session.persist(company);

            Employee employee = new Employee("Gosho", company);
            session.persist(employee);

            Building building = new Building(
                    "ul. nqkoq 100",
                    8,
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.01),
                    employee
            );
            session.persist(building);

            Apartment apartment = new Apartment(
                    1,
                    1,
                    BigDecimal.valueOf(100),
                    1,
                    building
            );
            session.persist(apartment);

            tx.commit();
        }
    }

    @AfterEach
    void cleanDB() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Occupant").executeUpdate();
            session.createMutationQuery("delete from Apartment").executeUpdate();
            session.createMutationQuery("delete from Building").executeUpdate();
            session.createMutationQuery("delete from Employee").executeUpdate();
            session.createMutationQuery("delete from Company").executeUpdate();
            tx.commit();
        }
    }

    @Test
    public void createOccupant_success() {
        long apartmentId;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartmentId = session.createQuery("select e.id from Employee e", Long.class)
                    .getSingleResult();
        }

        CreateOccupantDto dto = new CreateOccupantDto(12, "Pesho", true);



        Occupant occupant;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            OccupantDao.createOccupant(session, dto, apartmentId);
            transaction.commit();
            occupant = session.createQuery("from Occupant ", Occupant.class)
                    .getSingleResult();
        }

        assertEquals(12, occupant.getAge());
        assertEquals("Pesho", occupant.getName());
        assertEquals(apartmentId, occupant.getApartment().getId());
    }
}
