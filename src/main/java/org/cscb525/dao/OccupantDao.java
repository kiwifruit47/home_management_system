package org.cscb525.dao;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.occupant.CreateOccupantDto;
import org.cscb525.dto.occupant.OccupantDto;
import org.cscb525.dto.occupant.UpdateOccupantDto;
import org.cscb525.entity.*;
import org.cscb525.exceptions.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OccupantDao {
    public static void createOccupant(Session session, CreateOccupantDto occupantDto, long apartmentId) {
        Occupant occupant = new Occupant();
        occupant.setName(occupantDto.getName());
        occupant.setAge(occupantDto.getAge());
        occupant.setUsesElevator(occupantDto.usesElevator());
        occupant.setApartment(session.get(Apartment.class, apartmentId));

        session.persist(occupant);
    }

    public static void updateOccupant(UpdateOccupantDto occupantDto) {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Occupant occupant = session.get(Occupant.class, occupantDto.getId());
            if (occupant == null || occupant.isDeleted()) {
                throw new NotFoundException(Occupant.class, occupantDto.getId());
            }

            transaction = session.beginTransaction();

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

    public static OccupantDto findOccupantDtoById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OccupantDto> cr = cb.createQuery(OccupantDto.class);
            Root<Occupant> root = cr.from(Occupant.class);

            Join<Occupant, Apartment> apartment = root.join("apartment");

            cr.select(cb.construct(
                            OccupantDto.class,
                            root.get("name"),
                            root.get("age"),
                            apartment.get("apartmentNumber"),
                            apartment.get("floor")
                    ))
                    .where(cb.and(
                            cb.equal(root.get("id"), id),
                            cb.isFalse(root.get("deleted"))
                    ));
            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException(Occupant.class, id, e);
        }
    }

    public static OccupantDto findOccupantDtoById(Session session, long id) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<OccupantDto> cr = cb.createQuery(OccupantDto.class);
        Root<Occupant> root = cr.from(Occupant.class);

        Join<Occupant, Apartment> apartment = root.join("apartment");

        cr.select(cb.construct(
                        OccupantDto.class,
                        root.get("name"),
                        root.get("age"),
                        apartment.get("apartmentNumber"),
                        apartment.get("floor")
                ))
                .where(cb.and(
                        cb.equal(root.get("id"), id),
                        cb.isFalse(root.get("deleted"))
                ));
        return session.createQuery(cr).getSingleResult();
    }

    public static Occupant findOccupantById(Session session, long occupantId) {
        return session.get(Occupant.class, occupantId);
    }

    public static List<OccupantDto> findAllOccupants() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OccupantDto> cr = cb.createQuery(OccupantDto.class);
            Root<Occupant> root = cr.from(Occupant.class);

            Join<Occupant, Apartment> apartment = root.join("apartment");

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
                throw new NotFoundException(Occupant.class, id);
            }
            transaction.commit();
        }
    }

    public static void deleteAllOccupantsByCompany(Session session, long companyId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Occupant> update = cb.createCriteriaUpdate(Occupant.class);
        Root<Occupant> root = update.from(Occupant.class);

        Join<Occupant, Apartment> apartment = root.join("apartment");
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

    public static void restoreAllOccupantsByCompany(Session session, long companyId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Occupant> update = cb.createCriteriaUpdate(Occupant.class);
        Root<Occupant> root = update.from(Occupant.class);

        Join<Occupant, Apartment> apartment = root.join("apartment");
        Join<Apartment, Building> building = apartment.join("building");
        Join<Building, Employee> employee = building.join("employee");
        Join<Employee, Company> company = employee.join("company");

        update.set(root.get("deleted"), false)
                .where(cb.and(
                        cb.equal(company.get("id"), companyId),
                        cb.isTrue(root.get("deleted"))
                ));

        session.createMutationQuery(update).executeUpdate();
    }

    public static void deleteAllOccupantsByBuilding(Session session, long buildingId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Occupant> update = cb.createCriteriaUpdate(Occupant.class);
        Root<Occupant> root = update.from(Occupant.class);

        Join<Occupant, Apartment> apartment = root.join("apartment");
        Join<Apartment, Building> building = apartment.join("building");

        update.set(root.get("deleted"), true)
                .where(cb.and(
                        cb.equal(building.get("id"), buildingId),
                        cb.isFalse(root.get("deleted"))
                ));

        session.createMutationQuery(update).executeUpdate();
    }

    public static List<OccupantDto> occupantsFindByBuildingOrderByNameAsc(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OccupantDto> cr = cb.createQuery(OccupantDto.class);
            Root<Building> root = cr.from(Building.class);

            Join<Building, Apartment> apartment = root.join("apartments");
            Join<Apartment, Occupant> occupant = apartment.join("occupants");

            cr.select(cb.construct(
                            OccupantDto.class,
                            occupant.get("name"),
                            occupant.get("age"),
                            apartment.get("apartmentNumber"),
                            apartment.get("floor")
                    ))
                    .where(cb.and(
                            cb.equal(root.get("id"), buildingId),
                            cb.isFalse(root.get("deleted"))
                    ))
                    .orderBy(cb.asc(occupant.get("name")));
            return session.createQuery(cr).getResultList();
        }
    }

    public static List<OccupantDto> occupantsFindByBuildingOrderByAgeDesc(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<OccupantDto> cr = cb.createQuery(OccupantDto.class);
            Root<Building> root = cr.from(Building.class);

            Join<Building, Apartment> apartment = root.join("apartments");
            Join<Apartment, Occupant> occupant = apartment.join("occupants");

            cr.select(cb.construct(
                            OccupantDto.class,
                            occupant.get("name"),
                            occupant.get("age"),
                            apartment.get("apartmentNumber"),
                            apartment.get("floor")
                    ))
                    .where(
                            cb.and(
                                    cb.equal(root.get("id"), buildingId),
                                    cb.isFalse(root.get("deleted"))
                            )
                    )
                    .orderBy(cb.desc(occupant.get("age")));
            return session.createQuery(cr).getResultList();
        }
    }

    public static long findOccupantCountByBuilding(long buildingId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<Building> root = cr.from(Building.class);

            Join<Building, Apartment> apartment = root.join("apartments");
            Join<Apartment, Occupant> occupant = apartment.join("occupants");

            cr.select(cb.count(occupant))
                    .where(cb.and(
                            cb.equal(root.get("id"), buildingId),
                            cb.isFalse(root.get("deleted"))
                    ));
            return session.createQuery(cr).getSingleResult();
        }
    }
}
