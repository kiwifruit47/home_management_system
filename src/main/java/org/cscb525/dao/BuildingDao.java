package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.occupant.OccupantDto;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.Building;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BuildingDao {
    public static void createBuilding(Building building) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(building);
            transaction.commit();
        }
    }

    public static void updateBuilding(Building building) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(building);
            transaction.commit();
        }
    }

    public static Building getBuildingById(long id) {
        Building building;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            building = session.get(Building.class, id);
        }
        if (building == null)
            throw new EntityNotFoundException("Building with id " + id + " not found");
        return building;
    }

    public static List<Building> getAllBuildings() {
        List<Building> buildings;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            buildings = session.createQuery("select b from Building b", Building.class)
                    .getResultList();
        }
        return buildings;
    }

    public static void deleteBuilding(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Building b set b.deleted = true where b.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Building with id " + id + " not found");
            }
            transaction.commit();
        }
    }
    public static void restoreBuilding(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Building b set b.deleted = false where b.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("Building with id " + id + " not found.");
            }
            transaction.commit();
        }
    }


    public static List<Apartment> getAllApartmentsByBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Apartment> cr = cb.createQuery(Apartment.class);
            Root<Apartment> root = cr.from(Apartment.class);

            Join<?, ?> building = root.join("building");

            cr.select(root).where(cb.equal(building.get("id"), buildingId));

            return session.createQuery(cr).getResultList();
        }
    }

    public static long getApartmentCountByBuilding(long buildingId) {
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

    public static List<OccupantDto> getOccupantsByBuildingOrderByNameAsc(long buildingId) {
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
                    apartment.get("apartment_number"),
                    apartment.get("floor")
            ))
                    .where(cb.equal(root.get("id"), buildingId))
                    .orderBy(cb.asc(occupant.get("name")));
            return session.createQuery(cr).getResultList();
        }
    }

    public static List<OccupantDto> getOccupantsByBuildingOrderByAgeDesc(long buildingId) {
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
                            apartment.get("apartment_number"),
                            apartment.get("floor")
                    ))
                    .where(cb.equal(root.get("id"), buildingId))
                    .orderBy(cb.desc(occupant.get("age")));
            return session.createQuery(cr).getResultList();
        }
    }

    public static long getOccupantCountByBuilding(long buildingId) {
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
