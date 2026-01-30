package dao.occupant;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.OccupantDao;
import org.cscb525.dto.occupant.CreateOccupantDto;
import org.cscb525.dto.occupant.OccupantDto;
import org.cscb525.entity.*;
import org.cscb525.service.ApartmentService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultipleOccupantIT {
    @BeforeEach
    void setup() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company c1 = new Company("Company1");
            session.persist(c1);

            Company c2 = new Company("Company2");
            session.persist(c2);

            Company c3 = new Company("Company3");
            session.persist(c3);

            Employee e1 = new Employee("Sasho", c1);
            session.persist(e1);

            Employee e2 = new Employee("Gosho", c2);
            session.persist(e2);

            Employee e3 = new Employee("Pesho", c3);
            session.persist(e3);

            Building b1 = new Building(
                    "ul. nqkoq 100",
                    8,
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.01),
                    e1
            );
            session.persist(b1);

            Building b2 = new Building(
                    "ul. nqkoq 101",
                    8,
                    BigDecimal.valueOf(20),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.01),
                    e2
            );
            session.persist(b2);

            Building b3 = new Building(
                    "ul. nqkoq 102",
                    8,
                    BigDecimal.valueOf(30),
                    BigDecimal.valueOf(10),
                    BigDecimal.valueOf(0.01),
                    e3
            );
            session.persist(b3);

            Apartment a1 = new Apartment(
                    1,
                    1,
                    BigDecimal.valueOf(100),
                    1,
                    b1
            );
            session.persist(a1);

            Apartment a2 = new Apartment(
                    1,
                    1,
                    BigDecimal.valueOf(100),
                    1,
                    b2
            );
            session.persist(a2);

            Apartment a3 = new Apartment(
                    1,
                    1,
                    BigDecimal.valueOf(100),
                    1,
                    b3
            );
            session.persist(a3);

            transaction.commit();
        }

        ApartmentService apartmentService = new ApartmentService();
        CreateOccupantDto occupantDto1 = new CreateOccupantDto(20, "Ivan", true);
        CreateOccupantDto occupantDto2 = new CreateOccupantDto(20, "Maria", true);

        CreateOccupantDto occupantDto3 = new CreateOccupantDto(40, "Petko", true);
        CreateOccupantDto occupantDto4 = new CreateOccupantDto(8, "Sasho", true);
        CreateOccupantDto occupantDto5 = new CreateOccupantDto(12, "Mitko", true);

        CreateOccupantDto occupantDto6 = new CreateOccupantDto(75, "Gergana", true);
        CreateOccupantDto occupantDto7 = new CreateOccupantDto(75, "Goran", true);

        apartmentService.addOccupantToApartment(1, occupantDto1);
        apartmentService.addOccupantToApartment(1, occupantDto2);

        apartmentService.addOccupantToApartment(2, occupantDto3);
        apartmentService.addOccupantToApartment(2, occupantDto4);
        apartmentService.addOccupantToApartment(2, occupantDto5);

        apartmentService.addOccupantToApartment(3, occupantDto6);
        apartmentService.addOccupantToApartment(3, occupantDto7);
    }

    @AfterEach
    void cleanDB() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createMutationQuery("delete from Occupant").executeUpdate();
            session.createMutationQuery("delete from Apartment").executeUpdate();
            session.createMutationQuery("delete from Building").executeUpdate();
            session.createMutationQuery("delete from Employee ").executeUpdate();
            session.createMutationQuery("delete from Company").executeUpdate();
            transaction.commit();
        }
    }

    @Test
    public void findAllOccupants_success() {
        List<OccupantDto> occupants = OccupantDao.findAllOccupants();
        assertEquals(7, occupants.size());
    }

    @Test
    public void deleteAllOccupantsByCompany_success() {
        List<Occupant> occupants;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            OccupantDao.deleteAllOccupantsByCompany(session, 1L);
            transaction.commit();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Occupant> cr = cb.createQuery(Occupant.class);
            Root<Occupant> root = cr.from(Occupant.class);

            Join<Occupant, Apartment> apartment = root.join("apartment");
            Join<Apartment, Building> building = apartment.join("building");
            Join<Building, Employee> employee = building.join("employee");
            Join<Employee, Company> company = employee.join("company");

            cr.select(root).where(cb.and(
                    cb.equal(company.get("id"), 1),
                    cb.isFalse(root.get("deleted"))
            ));

            occupants = session.createQuery(cr).getResultList();
        }


        assertEquals(0, occupants.size());
    }

    @Test
    public void restoreAllOccupantsByCompany_success() {
        List<Occupant> occupants;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            OccupantDao.deleteAllOccupantsByCompany(session, 1L);
            transaction.commit();

            transaction = session.beginTransaction();
            OccupantDao.restoreAllOccupantsByCompany(session, 1L);
            transaction.commit();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Occupant> cr = cb.createQuery(Occupant.class);
            Root<Occupant> root = cr.from(Occupant.class);

            Join<Occupant, Apartment> apartment = root.join("apartment");
            Join<Apartment, Building> building = apartment.join("building");
            Join<Building, Employee> employee = building.join("employee");
            Join<Employee, Company> company = employee.join("company");

            cr.select(root).where(cb.and(
                    cb.equal(company.get("id"), 1),
                    cb.isFalse(root.get("deleted"))
            ));

            occupants = session.createQuery(cr).getResultList();
        }


        assertEquals(2, occupants.size());
    }

    @Test
    public void deleteAllOccupantsByBuilding_success() {
        List<Occupant> occupants;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            OccupantDao.deleteAllOccupantsByBuilding(session, 1L);
            transaction.commit();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Occupant> cr = cb.createQuery(Occupant.class);
            Root<Occupant> root = cr.from(Occupant.class);

            Join<Occupant, Apartment> apartment = root.join("apartment");
            Join<Apartment, Building> building = apartment.join("building");

            cr.select(root).where(cb.and(
                    cb.equal(building.get("id"), 1),
                    cb.isFalse(root.get("deleted"))
            ));

            occupants = session.createQuery(cr).getResultList();
        }


        assertEquals(0, occupants.size());
    }

    @Test
    public void occupantsFindByBuildingOrderByNameAsc_success() {
        List<OccupantDto> orderedOccupants = OccupantDao.occupantsFindByBuildingOrderByNameAsc(2L);

        assertEquals(3, orderedOccupants.size());
        assertEquals("Mitko", orderedOccupants.getFirst().getName());
        assertEquals("Petko", orderedOccupants.get(1).getName());
        assertEquals("Sasho", orderedOccupants.getLast().getName());

    }

    @Test
    public void occupantsFindByBuildingOrderByAgeDesc_success() {
        List<OccupantDto> orderedOccupants = OccupantDao.occupantsFindByBuildingOrderByAgeDesc(2L);

        assertEquals(3, orderedOccupants.size());
        assertEquals("Petko", orderedOccupants.getFirst().getName());
        assertEquals("Mitko", orderedOccupants.get(1).getName());
        assertEquals("Sasho", orderedOccupants.getLast().getName());
    }

    @Test
    public void findOccupantCountByBuilding_success() {
        Long count = OccupantDao.findOccupantCountByBuilding(2L);
        assertEquals(3, count);
    }
}
