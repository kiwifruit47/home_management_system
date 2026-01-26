package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.apartment.ApartmentDto;
import org.cscb525.dto.apartment.CreateApartmentDto;
import org.cscb525.dto.occupant.CreateOccupantDto;
import org.cscb525.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ApartmentDao {

    //apartment can't be persisted if Building doesn't exist in the DB
    public static void createApartment(CreateApartmentDto apartmentDto) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Building building = session.get(
                    Building.class,
                    apartmentDto.getBuildingId()
            );

            if (building == null || building.isDeleted()) {
                throw new EntityNotFoundException(
                        "Active building with id " + apartmentDto.getBuildingId() + " does not exist."
                );
            }

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
            transaction = session.beginTransaction();

            Apartment apartment = session.get(Apartment.class, apartmentId);
            if (apartment == null || apartment.isDeleted()) {
                throw new EntityNotFoundException(
                        "No active apartment with id " + apartmentId + " found."
                );
            }

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
                throw new EntityNotFoundException(
                        "No active apartment with id " + apartmentId + " found."
                );
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
            throw new EntityNotFoundException("No active apartment with id " + id + " found.");
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

    public static void deleteApartment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Apartment a set a.deleted = true where a.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Apartment with id " + id + " not found.");
            }
            transaction.commit();
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

    public static void restoreApartment(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Apartment a set a.deleted = false where a.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Apartment with id " + id + " not found.");
            }
            transaction.commit();
        }
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
