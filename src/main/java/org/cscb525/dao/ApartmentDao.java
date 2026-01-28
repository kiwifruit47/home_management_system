package org.cscb525.dao;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.apartment.ApartmentDto;
import org.cscb525.dto.apartment.CreateApartmentDto;
import org.cscb525.entity.*;
import org.cscb525.exceptions.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ApartmentDao {

    //apartment can't be persisted if Building doesn't exist in the DB
    public static void createApartment(CreateApartmentDto apartmentDto) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Building building = session.get(
                    Building.class,
                    apartmentDto.getBuildingId()
            );

            if (building == null || building.isDeleted()) {
                throw new NotFoundException(Building.class, apartmentDto.getBuildingId());
            }

            transaction = session.beginTransaction();

            Apartment apartment = new Apartment();
            apartment.setApartmentNumber(apartmentDto.getApartmentNumber());
            apartment.setArea(apartmentDto.getArea());
            apartment.setFloor(apartmentDto.getFloor());
            apartment.setPets(apartmentDto.getPets());
            apartment.setBuilding(building);

            session.persist(apartment);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static void updateApartmentPets(long apartmentId, int pets) {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Apartment apartment = session.get(Apartment.class, apartmentId);
            if (apartment == null || apartment.isDeleted()) {
                throw new NotFoundException(Apartment.class, apartmentId);
            }

            transaction = session.beginTransaction();

            apartment.setPets(pets);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static int findPetCountByApartment(long apartmentId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Apartment apartment = session.get(Apartment.class, apartmentId);
            if (apartment == null || apartment.isDeleted()) {
                throw new NotFoundException(Apartment.class, apartmentId);
            }
            return apartment.getPets();
        }
    }


    public static ApartmentDto findApartmentDtoById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ApartmentDto> cr = cb.createQuery(ApartmentDto.class);
            Root<Apartment> root = cr.from(Apartment.class);

            cr.select(cb.construct(
                    ApartmentDto.class,
                    root.get("floor"),
                    root.get("apartmentNumber"),
                    root.get("area"),
                    root.get("pets"),
                    root.get("building").get("address")
            ))
                    .where(cb.and(
                            cb.equal(root.get("id"), id),
                            cb.isFalse(root.get("deleted"))
                    ));

            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException(Apartment.class, id, e);
        }
    }

    public static Apartment findApartmentById(Session session, long apartmentId) {
        return session.get(Apartment.class, apartmentId);
    }
    public static List<ApartmentDto> findAllApartments() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ApartmentDto> cr = cb.createQuery(ApartmentDto.class);
            Root<Apartment> root = cr.from(Apartment.class);

            cr.select(cb.construct(
                            ApartmentDto.class,
                            root.get("floor"),
                            root.get("apartmentNumber"),
                            root.get("area"),
                            root.get("pets"),
                            root.get("building").get("address")
                    ))
                    .where(cb.isFalse(root.get("deleted")));
            return session.createQuery(cr).getResultList();
        }
    }

    public static List<Long> findAllApartmentIds() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<Apartment> root = cr.from(Apartment.class);

            cr.select(root.get("id"))
                    .where(cb.isFalse(root.get("deleted")));

            return session.createQuery(cr).getResultList();
        }
    }

    public static void deleteAllApartmentsByCompany(Session session, long companyId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Apartment> update = cb.createCriteriaUpdate(Apartment.class);
        Root<Apartment> root = update.from(Apartment.class);

        Join<Apartment, Building> building = root.join("building");
        Join<Building, Employee> employee = building.join("employee");
        Join<Employee, Company> company = employee.join("company");

        update.set(root.get("deleted"), true)
                .where(cb.and(
                        cb.equal(company.get("id"), companyId),
                        cb.isFalse(root.get("deleted"))
                ));

        session.createMutationQuery(update).executeUpdate();
    }

    public static void restoreAllApartmentsByCompany(Session session, long companyId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Apartment> update = cb.createCriteriaUpdate(Apartment.class);
        Root<Apartment> root = update.from(Apartment.class);

        Join<Apartment, Building> building = root.join("building");
        Join<Building, Employee> employee = building.join("employee");
        Join<Employee, Company> company = employee.join("company");

        update.set(root.get("deleted"), false)
                .where(cb.and(
                        cb.equal(company.get("id"), companyId),
                        cb.isTrue(root.get("deleted"))
                ));

        session.createMutationQuery(update).executeUpdate();
    }

    public static void deleteAllApartmentsByBuilding(Session session, long buildingId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Apartment> update = cb.createCriteriaUpdate(Apartment.class);
        Root<Apartment> root = update.from(Apartment.class);

        Join<Apartment, Building> building = root.join("building");

        update.set(root.get("deleted"), true)
                .where(cb.and(
                        cb.equal(building.get("id"), buildingId),
                        cb.isFalse(root.get("deleted"))
                ));
        session.createMutationQuery(update).executeUpdate();
    }

    public static long findApartmentCountByBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<Apartment> root = cr.from(Apartment.class);

            Join<Apartment, Building> building = root.join("building");

            cr.select(cb.construct(
                            Long.class,
                            cb.count(root)
                    ))
                    .where(cb.and(
                            cb.equal(building.get("id"), buildingId),
                            cb.isFalse(root.get("deleted"))
                    ));

            return session.createQuery(cr).getSingleResult();
        }
    }

    public static List<ApartmentDto> findAllApartmentsByBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ApartmentDto> cr = cb.createQuery(ApartmentDto.class);
            Root<Apartment> root = cr.from(Apartment.class);

            Join<Apartment, Building> building = root.join("building");

            cr.select(cb.construct(
                    ApartmentDto.class,
                    root.get("floor"),
                    root.get("apartmentNumber"),
                    root.get("area"),
                    root.get("pets"),
                    root.get("building").get("address")
            ))
                    .where(
                            cb.and(
                                    cb.equal(building.get("id"), buildingId),
                                    cb.isFalse(root.get("deleted"))
                            )
                    );

            return session.createQuery(cr).getResultList();
        }
    }

}
