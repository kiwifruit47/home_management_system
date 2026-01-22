package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.occupant.CreateOccupantDto;
import org.cscb525.dto.occupant.OccupantDto;
import org.cscb525.dto.occupant.UpdateOccupantDto;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.Building;
import org.cscb525.entity.Occupant;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OccupantDao {
    //occupant can't be persisted if the apartment they live in doesn't exist in the DB
    public static void createOccupant(CreateOccupantDto occupantDto) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Apartment apartment = session.get(
                    Apartment.class,
                    occupantDto.getApartmentId()
            );
            if (apartment == null)
                throw new EntityNotFoundException("Apartment with id " + occupantDto.getApartmentId() + " does not exist.");

            Occupant occupant = new Occupant();
            occupant.setName(occupantDto.getName());
            occupant.setAge(occupantDto.getAge());
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

    public static void updateOccupant(UpdateOccupantDto occupantDto) {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Occupant occupant = session.get(Occupant.class, occupantDto.getId());
            if (occupant == null) {
                throw new EntityNotFoundException(
                        "No occupant with id " + occupantDto.getId() + " found."
                );
            }

            occupant.setAge(occupantDto.getAge());
            occupant.setName(occupantDto.getName());
            occupant.setUsesElevator(occupantDto.isUsesElevator());

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static OccupantDto findOccupantById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OccupantDto> cr = cb.createQuery(OccupantDto.class);
            Root<Occupant> root = cr.from(Occupant.class);

            Join<?, ?> apartment = root.join("apartment");

            cr.select(cb.construct(
                            OccupantDto.class,
                            root.get("name"),
                            root.get("age"),
                            apartment.get("apartmentNumber"),
                            apartment.get("floor")
                    ))
                    .where(cb.equal(root.get("id"), id));
            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No occupant with id " + id + " found.");
        }
    }

    public static List<OccupantDto> findAllOccupants() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OccupantDto> cr = cb.createQuery(OccupantDto.class);
            Root<Occupant> root = cr.from(Occupant.class);

            Join<?, ?> apartment = root.join("apartment");

            cr.select(cb.construct(
                            OccupantDto.class,
                            root.get("name"),
                            root.get("age"),
                            apartment.get("apartmentNumber"),
                            apartment.get("floor")
                    ))
                    .where(cb.isFalse(root.get("deleted")));;
            return session.createQuery(cr).getResultList();
        }
    }

    public static void deleteOccupant(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Occupant o set o.deleted = true where o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Occupant with id " + id + " not found.");
            }
            transaction.commit();
        }
    }

    public static void restoreOccupant(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Occupant o set o.deleted = false where o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Occupant with id " + id + " not found.");
            }
            transaction.commit();
        }
    }

    public static List<OccupantDto> findOccupantsByBuildingOrderByNameAsc(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OccupantDto> cr = cb.createQuery(OccupantDto.class);
            Root<Building> root = cr.from(Building.class);

            Join<?, ?> apartment = root.join("apartments");
            Join<?, ?> occupant = apartment.join("occupants");

            cr.select(cb.construct(
                            OccupantDto.class,
                            occupant.get("name"),
                            occupant.get("age"),
                            apartment.get("apartmentNumber"),
                            apartment.get("floor")
                    ))
                    .where(cb.equal(root.get("id"), buildingId))
                    .orderBy(cb.asc(occupant.get("name")));
            return session.createQuery(cr).getResultList();
        }
    }

    public static List<OccupantDto> findOccupantsByBuildingOrderByAgeDesc(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OccupantDto> cr = cb.createQuery(OccupantDto.class);
            Root<Building> root = cr.from(Building.class);

            Join<?, ?> apartment = root.join("apartments");
            Join<?, ?> occupant = apartment.join("occupants");

            cr.select(cb.construct(
                            OccupantDto.class,
                            occupant.get("name"),
                            occupant.get("age"),
                            apartment.get("apartmentNumber"),
                            apartment.get("floor")
                    ))
                    .where(cb.equal(root.get("id"), buildingId))
                    .orderBy(cb.desc(occupant.get("age")));
            return session.createQuery(cr).getResultList();
        }
    }

    public static long findOccupantCountByBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<Building> root = cr.from(Building.class);

            Join<?, ?> apartment = root.join("apartments");
            Join<?, ?> occupant = apartment.join("occupants");

            cr.select(cb.construct(
                            Long.class,
                            cb.count(occupant)
                    ))
                    .where(cb.equal(root.get("id"), buildingId));
            return session.createQuery(cr).getSingleResult();
        }
    }
}
