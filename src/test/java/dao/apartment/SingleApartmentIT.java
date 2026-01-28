package dao.apartment;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.ApartmentDao;
import org.cscb525.entity.Apartment;
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

import static org.junit.jupiter.api.Assertions.*;

public class SingleApartmentIT {
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
                    0,
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
            session.createMutationQuery("delete from Apartment").executeUpdate();
            session.createMutationQuery("delete from Building").executeUpdate();
            session.createMutationQuery("delete from Employee").executeUpdate();
            session.createMutationQuery("delete from Company").executeUpdate();

            tx.commit();
        }
    }

    @Test
    public void updateApartmentPet_apartmentDeleted_Success() {
        long apartmentId;
        Apartment apartment;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartmentId = session.createQuery("select a.id from Apartment a", Long.class)
                    .getSingleResult();
        }
        ApartmentDao.updateApartmentPets(apartmentId, 2);
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartment = session.createQuery("from Apartment ", Apartment.class)
                    .getSingleResult();
        }
        assertEquals(2, apartment.getPets());
    }

    @Test
    public void updateApartmentPets_apartmentNotFound_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> ApartmentDao.updateApartmentPets(5L, 1)
        );
    }

    @Test
    public void updateApartmentPet_apartmentDeleted_throwsException() {
        long apartmentId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Apartment apartment = session
                    .createQuery("from Apartment a", Apartment.class)
                    .getSingleResult();
            apartmentId = apartment.getId();
            Transaction transaction = session.beginTransaction();
            apartment.setDeleted(true);
            transaction.commit();
        }

        assertThrows(
                NotFoundException.class,
                () -> ApartmentDao.updateApartmentPets(apartmentId, 1)
        );
    }

    @Test
    public void findPetCountByApartment_Success() {
        long apartmentId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartmentId = session.createQuery("select a.id from Apartment a", Long.class)
                    .getSingleResult();
        }

        int petCount = ApartmentDao.findPetCountByApartment(apartmentId);
        assertEquals(0, petCount);
    }

    @Test
    public void findPetCountByApartment_apartmentDeleted_throwsException() {
        long apartmentId;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Apartment apartment = session
                    .createQuery("from Apartment a", Apartment.class)
                    .getSingleResult();
            apartmentId = apartment.getId();
            Transaction transaction = session.beginTransaction();
            apartment.setDeleted(true);
            transaction.commit();
        }

        assertThrows(
                NotFoundException.class,
                () -> ApartmentDao.findPetCountByApartment(apartmentId)
        );
    }

    @Test
    public void findPetCountByApartment_apartmentNotFound_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> ApartmentDao.findPetCountByApartment(5L)
        );
    }

    @Test
    public void findApartmentDtoById_Success() {
        long apartmentId;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartmentId = session
                    .createQuery("select a.id from Apartment a", Long.class)
                    .getSingleResult();
        }

        var dto = ApartmentDao.findApartmentDtoById(apartmentId);

        assertEquals(1, dto.getFloor());
        assertEquals(1, dto.getApartmentNumber());
        assertEquals(0, dto.getPets());
        assertEquals("ul. nqkoq 100", dto.getBuildingAddress());
        assertEquals(0,
                BigDecimal.valueOf(100).compareTo(dto.getArea())
        );
    }

    @Test
    public void findApartmentDtoById_apartmentDeleted_throwsException() {
        long apartmentId;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Apartment apartment = session
                    .createQuery("from Apartment a", Apartment.class)
                    .getSingleResult();
            apartmentId = apartment.getId();

            Transaction tx = session.beginTransaction();
            apartment.setDeleted(true);
            tx.commit();
        }

        assertThrows(
                NotFoundException.class,
                () -> ApartmentDao.findApartmentDtoById(apartmentId)
        );
    }

    @Test
    public void findApartmentDtoById_apartmentNotFound_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> ApartmentDao.findApartmentDtoById(999L)
        );
    }

    @Test
    public void findApartmentById_Success() {
        long apartmentId;
        Apartment apartment;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartmentId = session
                    .createQuery("select a.id from Apartment a", Long.class)
                    .getSingleResult();

            apartment = ApartmentDao.findApartmentById(session, apartmentId);
        }

        assertEquals(apartmentId, apartment.getId());
    }

    @Test
    public void findApartmentById_apartmentNotFound_returnsNull() {
        Apartment apartment;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartment = ApartmentDao.findApartmentById(session, 999L);
        }

        assertNull(apartment);
    }

}
