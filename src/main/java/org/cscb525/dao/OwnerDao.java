package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.owner.CreateOwnerDto;
import org.cscb525.dto.owner.OwnerDto;
import org.cscb525.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OwnerDao {
    public static void createOwner(CreateOwnerDto ownerDto) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Owner owner = new Owner();
            owner.setName(ownerDto.getName());
            session.persist(owner);
            transaction.commit();
        }
    }

    public static void updateOwnerName(long ownerId, String name) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Owner owner = session.get(
                    Owner.class,
                    ownerId
            );

            if (owner == null || owner.isDeleted()) {
                throw new EntityNotFoundException("Active owner with id " + ownerId + " not found.");
            }
            owner.setName(name);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public static OwnerDto findOwnerDtoById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OwnerDto> cr = cb.createQuery(OwnerDto.class);
            Root<Owner> root = cr.from(Owner.class);

            cr.select(cb.construct(
                    OwnerDto.class,
                    root.get("name")
            ))
                    .where(cb.and(
                            cb.equal(root.get("id"), id),
                            cb.isFalse(root.get("deleted"))
                    ));

            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("Owner with id " + id + " not found.");
        }
    }

    public static Owner findOwnerById(Session session, long ownerId) {
        return session.get(Owner.class, ownerId);
    }

    public static List<OwnerDto> getAllOwners() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OwnerDto> cr = cb.createQuery(OwnerDto.class);
            Root<Owner> root = cr.from(Owner.class);

            cr.select(cb.construct(
                            OwnerDto.class,
                            root.get("name")
                    ))
                    .where(cb.isFalse(root.get("deleted")));

            return session.createQuery(cr).getResultList();
        }
    }

    public static void deleteOwner(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Owner o set o.deleted = true where o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("owner with id " + id + " not found.");
            }
            transaction.commit();
        }
    }

    public static void deleteAllOwnersByCompany(Session session, long companyId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Owner> update = cb.createCriteriaUpdate(Owner.class);
        Root<Owner> root = update.from(Owner.class);

        Join<Owner, Apartment> apartment = root.join("apartment");
        Join<Apartment, Building> building = apartment.join("building");
        Join<Building, Employee> employee = building.join("employee");
        Join<Employee, Company> company = employee.join("company");

        update.set(root.get("deleted"), true)
                .where(cb.and(
                        cb.equal(company.get("id"), companyId),
                        cb.isFalse(root.get("deleted"))
                ));

        session.createMutationQuery(update).executeUpdate();
    }

    public static void deleteAllOwnersByBuilding(Session session, long buildingId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Owner> update = cb.createCriteriaUpdate(Owner.class);
        Root<Owner> root = update.from(Owner.class);

        Join<Owner, Apartment> apartment = root.join("apartment");
        Join<Apartment, Building> building = apartment.join("building");

        update.set(root.get("deleted"), true)
                .where(cb.and(
                        cb.equal(building.get("id"), buildingId),
                        cb.isFalse(root.get("deleted"))
                ));

        session.createMutationQuery(update).executeUpdate();
    }

    public static void restoreOwner(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int updatedRows = session.createQuery("update Owner o set o.deleted = false where o.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            if (updatedRows == 0) {
                transaction.rollback();
                throw new EntityNotFoundException("owner with id " + id + " not found.");
            }
            transaction.commit();
        }
    }

    public static List<OwnerDto> findAllOwnersByApartment(long apartmentId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OwnerDto> cr = cb.createQuery(OwnerDto.class);
            Root<Owner> root = cr.from(Owner.class);

            Join<Owner, Apartment> apartments = root.join("apartments");

            cr.select(cb.construct(
                    OwnerDto.class,
                    root.get("name")
            ))
                    .where(cb.and(
                            cb.equal(apartments.get("id"), apartmentId),
                            cb.isFalse(root.get("deleted")),
                            cb.isFalse(apartments.get("deleted"))
                    ));
            return session.createQuery(cr).getResultList();
        }
    }
}
