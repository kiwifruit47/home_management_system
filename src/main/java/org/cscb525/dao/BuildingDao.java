package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.cscb525.config.SessionFactoryUtil;
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


    public static List<Apartment> getAllApartmentsByBuilding(Building building) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Apartment> cr = cb.createQuery(Apartment.class);
            Root<Apartment> root = cr.from(Apartment.class);

            cr.select(root).where(cb.equal(root.get("building"), building));

            return session.createQuery(cr).getResultList();
        }
    }
}
