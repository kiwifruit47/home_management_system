package dao.owner;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.OwnerDao;
import org.cscb525.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultipleOwnerIT {
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

            Owner o1 = new Owner("Dancho");
            session.persist(o1);

            Owner o2 = new Owner("Pencho");
            session.persist(o2);

            Owner o3 = new Owner("Maria");
            session.persist(o3);

            o1.getApartments().add(a1);
            o2.getApartments().add(a1);
            o3.getApartments().add(a1);
            a1.getOwners().add(o1);
            a1.getOwners().add(o2);
            a1.getOwners().add(o3);

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
    public void findAllOwners_success() {
        assertEquals(3, OwnerDao.findAllOwners().size());
    }

    @Test
    public void deleteAllOwnersByCompany_success() {
        List<Owner> owners;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            OwnerDao.deleteAllOwnersByCompany(session, 1);
            transaction.commit();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Owner> cr = cb.createQuery(Owner.class);
            Root<Owner> root = cr.from(Owner.class);

            cr.select(root).where(cb.and(
                    cb.equal(
                            root.get("apartments").get("building").get("employee").get("company").get("id"),
                            1
                    ),
                    cb.isFalse(root.get("deleted"))
            ));

            owners = session.createQuery(cr).getResultList();
        }

        assertEquals(0, owners.size());
    }

    @Test
    public void restoreAllOwnersByCompany_success() {
        List<Owner> owners;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            OwnerDao.deleteAllOwnersByCompany(session, 1);
            transaction.commit();

            transaction = session.beginTransaction();
            OwnerDao.restoreAllOwnersByCompany(session, 1);
            transaction.commit();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Owner> cr = cb.createQuery(Owner.class);
            Root<Owner> root = cr.from(Owner.class);

            cr.select(root).where(cb.and(
                    cb.equal(
                            root.get("apartments").get("building").get("employee").get("company").get("id"),
                            1
                    ),
                    cb.isFalse(root.get("deleted"))
            ));

            owners = session.createQuery(cr).getResultList();
        }

        assertEquals(3, owners.size());
    }

    @Test
    public void deleteAllOwnersByBuilding_success() {
        List<Owner> owners;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            OwnerDao.deleteAllOwnersByBuilding(session, 1);
            transaction.commit();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Owner> cr = cb.createQuery(Owner.class);
            Root<Owner> root = cr.from(Owner.class);

            cr.select(root).where(cb.and(
                    cb.equal(
                            root.get("apartments").get("building").get("id"),
                            1
                    ),
                    cb.isFalse(root.get("deleted"))
            ));

            owners = session.createQuery(cr).getResultList();
        }

        assertEquals(0, owners.size());
    }

    @Test
    public void findAllOwnersByApartment_success() {
        assertEquals(3, OwnerDao.findAllOwnersByApartment(1).size());
    }
}
