package org.cscb525.dao;

import org.cscb525.config.SessionFactoryUtil;
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
            Transaction transaction = session.beginTransaction();
            building = session.get(Building.class, id);
            transaction.commit();
        }
        return building;
    }

    public static List<Building> getAllBuildings() {
        List<Building> buildings;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            buildings = session.createQuery("select b from Building b", Building.class)
                    .getResultList();
            transaction.commit();
        }
        return buildings;
    }

    public static void deleteBuilding(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("update Building b set b.deleted = true where b.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        }
    }
    public static void restoreBuilding(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("update Building b set b.deleted = false where b.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
        }
    }
}
