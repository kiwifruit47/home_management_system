package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
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
    public static void createApartment(@Valid CreateApartmentDto apartmentDto) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Building building = session.get(
                    Building.class,
                    apartmentDto.getBuildingId()
            );

            if (building == null) {
                throw new EntityNotFoundException(
                        "Building with id " + apartmentDto.getBuildingId() + " does not exist."
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
            if (apartment == null) {
                throw new EntityNotFoundException(
                        "No apartment with id " + apartmentId + " found."
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

    public static void addOwnerToApartment(long apartmentId, long ownerId) {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Apartment apartment = session.get(Apartment.class, apartmentId);
            Owner owner = session.get(Owner.class, ownerId);

            if (apartment == null || owner == null) {
                throw new EntityNotFoundException("Apartment or owner not found.");
            }

            apartment.getOwners().add(owner);
            owner.getApartments().add(apartment);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static void removeOwnerFromApartment(long apartmentId, long ownerId) {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Apartment apartment = session.get(Apartment.class, apartmentId);
            Owner owner = session.get(Owner.class, ownerId);

            if (apartment == null || owner == null) {
                throw new EntityNotFoundException("Apartment or owner not found.");
            }

            if (!apartment.getOwners().contains(owner)) {
                throw new IllegalStateException("This owner is not associated with this apartment.");
            }

            apartment.getOwners().remove(owner);
            owner.getApartments().remove(apartment);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static void addOccupantToApartment(long apartmentId, CreateOccupantDto occupantDto) {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Apartment apartment = session.get(Apartment.class, apartmentId);
            if (apartment == null) {
                throw new EntityNotFoundException("Apartment not found.");
            }

            Occupant occupant = new Occupant();
            occupant.setName(occupantDto.getName());
            occupant.setAge(occupant.getAge());
            occupant.setUsesElevator(occupantDto.usesElevator());
            occupant.setApartment(apartment);

            session.persist(occupant);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static void removeOccupant(long occupantId) {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Occupant occupant = session.get(Occupant.class, occupantId);
            if (occupant == null) {
                throw new EntityNotFoundException("Occupant not found.");
            }

            session.remove(occupant);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static ApartmentDto findApartmentById(long id) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
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
                    .where(cb.equal(root.get("id"), id));

            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No apartment with id " + id + " found.");
        }
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

            cr.select(root.get("id"));

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

            Join<?, ?> building = root.join("building");

            cr.select(cb.construct(
                            Long.class,
                            cb.count(root)
                    ))
                    .where(cb.equal(building.get("id"), buildingId));

            return session.createQuery(cr).getSingleResult();
        }
    }

    public static List<ApartmentDto> findAllApartmentsByBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ApartmentDto> cr = cb.createQuery(ApartmentDto.class);
            Root<Apartment> root = cr.from(Apartment.class);

            Join<?, ?> building = root.join("building");

            cr.select(cb.construct(
                    ApartmentDto.class,
                    root.get("floor"),
                    root.get("apartmentNumber"),
                    root.get("area"),
                    root.get("pets"),
                    root.get("building").get("address")
            ))
                    .where(cb.equal(building.get("id"), buildingId));

            return session.createQuery(cr).getResultList();
        }
    }

}
