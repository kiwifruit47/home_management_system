package dao.occupant;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.OccupantDao;
import org.cscb525.dto.occupant.UpdateOccupantDto;
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

public class SingleOccupantIT {
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

            Occupant occupant = new Occupant(
                    20,
                    "Ivan",
                    true,
                    apartment
            );
            session.persist(occupant);

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
    public void updateOccupant_success() {
        UpdateOccupantDto dto = new UpdateOccupantDto(1L, 21, "Ivan", true);
        OccupantDao.updateOccupant(dto);
        assertEquals(21, OccupantDao.findOccupantDtoById(1L).getAge());
    }

    @Test
    public void updateOccupant_nullOccupant_throwsException() {
        UpdateOccupantDto dto = new UpdateOccupantDto(5L, 21, "Ivan", true);
        assertThrows(
                NotFoundException.class,
                () -> OccupantDao.updateOccupant(dto)
        );
    }

    @Test
    public void updateOccupant_deletedOccupant_throwsException() {
        OccupantDao.deleteOccupant(1L);
        UpdateOccupantDto dto = new UpdateOccupantDto(1L, 21, "Ivan", true);
        assertThrows(
                NotFoundException.class,
                () -> OccupantDao.updateOccupant(dto)
        );
    }

    @Test
    public void findOccupantDtoById_success() {
        assertEquals("Ivan", OccupantDao.findOccupantDtoById(1L).getName());
    }

    @Test
    public void findOccupantDtoById_nullOccupant_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> OccupantDao.findOccupantDtoById(5L)
        );
    }

    @Test
    public void findOccupantDtoById_throwsException() {
        OccupantDao.deleteOccupant(1L);
        assertThrows(
                NotFoundException.class,
                () -> OccupantDao.findOccupantDtoById(1L)
        );
    }

    @Test
    public void deleteOccupant_success() {
        OccupantDao.deleteOccupant(1L);
        assertThrows(
                NotFoundException.class,
                () -> OccupantDao.findOccupantDtoById(1L)
        );
    }

    @Test
    public void deleteOccupant_nullOccupant_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> OccupantDao.findOccupantDtoById(5L)
        );
    }
}
