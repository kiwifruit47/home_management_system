package dao.owner;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.OwnerDao;
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

public class SingleOwnerIT {
    @BeforeEach
    void setup() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company c1 = new Company("Company1");
            session.persist(c1);

            Employee e1 = new Employee("Sasho", c1);
            session.persist(e1);

            Building b1 = new Building(
                    "ul. nqkoq 100",
                    8,
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.01),
                    e1
            );
            session.persist(b1);

            Apartment a1 = new Apartment(
                    1,
                    1,
                    BigDecimal.valueOf(100),
                    1,
                    b1
            );
            session.persist(a1);

            Owner owner = new Owner("Dancho");
            session.persist(owner);

            owner.getApartments().add(a1);
            a1.getOwners().add(owner);

            transaction.commit();
        }
    }

    @AfterEach
    void cleanDB() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createMutationQuery("delete from Owner").executeUpdate();
            session.createMutationQuery("delete from Apartment").executeUpdate();
            session.createMutationQuery("delete from Building").executeUpdate();
            session.createMutationQuery("delete from Employee ").executeUpdate();
            session.createMutationQuery("delete from Company").executeUpdate();
            transaction.commit();
        }
    }

    @Test
    public void updateOwnerName_success() {
        OwnerDao.updateOwnerName(1, "Yordan");
        assertEquals("Yordan", OwnerDao.findOwnerDtoById(1).getName());
    }

    @Test
    public void updateOwnerName_nullOwner_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> OwnerDao.updateOwnerName(2, "Yordan")
        );
    }

    @Test
    public void findOwnerDtoById_success() {
        assertEquals("Dancho", OwnerDao.findOwnerDtoById(1).getName());
    }

    @Test
    public void findOwnerDtoById_nullOwner_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> OwnerDao.findOwnerDtoById(2)
        );
    }

    @Test
    public void deleteOwner_success() {
        OwnerDao.deleteOwner(1);
        assertThrows(
                NotFoundException.class,
                () -> OwnerDao.findOwnerDtoById(1)
        );
    }

    @Test
    public void deleteOwner_nullOwner_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> OwnerDao.deleteOwner(2)
        );
    }


}
