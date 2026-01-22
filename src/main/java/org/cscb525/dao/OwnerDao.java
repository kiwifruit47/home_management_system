package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.owner.CreateOwnerDto;
import org.cscb525.dto.owner.OwnerDto;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.Owner;
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

            if (owner == null) {
                throw new EntityNotFoundException("Owner with id " + ownerId + " not found.");
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

    public static void removeApartmentFromOwner(long ownerId, long apartmentId) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Owner owner = session.get(
                    Owner.class,
                    ownerId
            );

            Apartment apartment = session.get(
                    Apartment.class,
                    apartmentId
            );

            if (owner == null || apartment == null) {
                throw new EntityNotFoundException("Owner or apartment not found.");
            }

            if (!owner.getApartments().contains(apartment)) {
                throw new IllegalStateException("This apartment is not associated with this owner.");
            }

            owner.getApartments().remove(apartment);
            apartment.getOwners().remove(owner);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static OwnerDto getOwnerById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OwnerDto> cr = cb.createQuery(OwnerDto.class);
            Root<Owner> root = cr.from(Owner.class);

            cr.select(cb.construct(
                    OwnerDto.class,
                    root.get("name")
            ))
                    .where(cb.equal(root.get("id"), id));

            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("Owner with id " + id + " not found.");
        }
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
}
