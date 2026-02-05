package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.building.BuildingDto;
import org.cscb525.dto.building.CreateBuildingDto;
import org.cscb525.dto.building.UpdateBuildingDto;
import org.cscb525.entity.Building;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.cscb525.exceptions.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BuildingDao {
    public static void createBuilding(Session session, CreateBuildingDto buildingDto) {
        Employee employee = session.get(Employee.class, buildingDto.getEmployeeId());

        if (employee == null || employee.isDeleted()) {
            throw new NotFoundException(Employee.class, buildingDto.getEmployeeId());
        }


        Building building = new Building();
        building.setAddress(buildingDto.getAddress());
        building.setEmployee(employee);
        building.setFloors(buildingDto.getFloors());
        building.setMonthlyTaxPerPerson(buildingDto.getMonthlyTaxPerPerson());
        building.setMonthlyTaxPerM2(buildingDto.getMonthlyTaxPerM2());
        building.setMonthlyTaxPerPet(buildingDto.getMonthlyTaxPerPet());

        session.persist(building );
    }

    public static void updateBuilding(UpdateBuildingDto buildingDto) {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Building building = session.get(Building.class, buildingDto.getId());
            if (building == null || building.isDeleted()) {
                throw new NotFoundException(Building.class, buildingDto.getId());
            }

            transaction = session.beginTransaction();

            building.setMonthlyTaxPerPerson(buildingDto.getMonthlyTaxPerPerson());
            building.setMonthlyTaxPerM2(buildingDto.getMonthlyTaxPerM2());
            building.setMonthlyTaxPerPet(buildingDto.getMonthlyTaxPerPet());

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static BuildingDto findBuildingDtoById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BuildingDto> cr = cb.createQuery(BuildingDto.class);
            Root<Building> root = cr.from(Building.class);

            cr.select(cb.construct(
                    BuildingDto.class,
                    root.get("id"),
                    root.get("address"),
                    root.get("floors"),
                    root.get("monthlyTaxPerPerson"),
                    root.get("monthlyTaxPerPet"),
                    root.get("monthlyTaxPerM2"),
                    root.get("employee").get("name")
            ))
                    .where(cb.and(
                            cb.equal(root.get("id"), id),
                            cb.isFalse(root.get("deleted"))
                    ));
            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException(Building.class, id, e);
        }
    }

    public static BuildingDto findBuildingDtoById(Session session, long id) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<BuildingDto> cr = cb.createQuery(BuildingDto.class);
        Root<Building> root = cr.from(Building.class);

        cr.select(cb.construct(
                        BuildingDto.class,
                        root.get("id"),
                        root.get("address"),
                        root.get("floors"),
                        root.get("monthlyTaxPerPerson"),
                        root.get("monthlyTaxPerPet"),
                        root.get("monthlyTaxPerM2"),
                        root.get("employee").get("name")
                ))
                .where(cb.and(
                        cb.equal(root.get("id"), id),
                        cb.isFalse(root.get("deleted"))
                ));
        return session.createQuery(cr).getSingleResult();
    }

    public static List<BuildingDto> findAllBuildings() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BuildingDto> cr = cb.createQuery(BuildingDto.class);
            Root<Building> root = cr.from(Building.class);

            cr.select(cb.construct(
                            BuildingDto.class,
                            root.get("id"),
                            root.get("address"),
                            root.get("floors"),
                            root.get("monthlyTaxPerPerson"),
                            root.get("monthlyTaxPerPet"),
                            root.get("monthlyTaxPerM2"),
                            root.get("employee").get("name")
                    ))
                    .where(cb.isFalse(root.get("deleted")));
            return session.createQuery(cr).getResultList();
        }
    }

    public static void deleteBuilding(Session session, long id) {
       session.createMutationQuery("update Building b set b.deleted = true where b.id = :id")
               .setParameter("id", id)
               .executeUpdate();
    }

    public static void deleteAllBuildingsByCompany(Session session, long companyId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Building> update = cb.createCriteriaUpdate(Building.class);
        Root<Building> root = update.from(Building.class);

        Join<Building, Employee> employee = root.join("employee");
        Join<Employee, Company> company = employee.join("company");

        update.set(root.get("deleted"), true)
                .where(
                        cb.and(
                                cb.equal(company.get("id"), companyId),
                                cb.isFalse(root.get("deleted"))
                        )
                );
        session.createMutationQuery(update).executeUpdate();
    }

    public static void restoreAllBuildingsByCompany(Session session, long companyId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<Building> update = cb.createCriteriaUpdate(Building.class);
        Root<Building> root = update.from(Building.class);

        Join<Building, Employee> employee = root.join("employee");
        Join<Employee, Company> company = employee.join("company");

        update.set(root.get("deleted"), false)
                .where(
                        cb.and(
                                cb.equal(company.get("id"), companyId),
                                cb.isTrue(root.get("deleted"))
                        )
                );
        session.createMutationQuery(update).executeUpdate();
    }

    public static long findAllBuildingCountByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<Building> root = cr.from(Building.class);

            Join<Building, Employee> employee = root.join("employee");
            Join<Employee, Company> company = employee.join("company");

            cr.select(cb.count(root))
                    .where(cb.and(
                            cb.equal(company.get("id"), companyId),
                            cb.isFalse(root.get("deleted"))
                    ));

            return session.createQuery(cr).getSingleResult();
        }
    }

    public static List<BuildingDto> findAllBuildingsByEmployee(long employeeId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BuildingDto> cr = cb.createQuery(BuildingDto.class);
            Root<Building> root = cr.from(Building.class);

            Join<Building, Employee> employee = root.join("employee");

            cr.select(cb.construct(
                            BuildingDto.class,
                            root.get("id"),
                            root.get("address"),
                            root.get("floors"),
                            root.get("monthlyTaxPerPerson"),
                            root.get("monthlyTaxPerPet"),
                            root.get("monthlyTaxPerM2"),
                            root.get("employee").get("name")
                    ))
                    .where(cb.and(
                            cb.equal(employee.get("id"), employeeId),
                            cb.isFalse(root.get("deleted"))
                    ));
            return session.createQuery(cr).getResultList();
        }
    }

    public static  List<BuildingDto> findAllBuildingsByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BuildingDto> cr = cb.createQuery(BuildingDto.class);
            Root<Building> root = cr.from(Building.class);

            Join<Building, Employee> employee = root.join("employee");
            Join<Employee, Company> company = employee.join("company");

            cr.select(cb.construct(
                            BuildingDto.class,
                            root.get("id"),
                            root.get("address"),
                            root.get("floors"),
                            root.get("monthlyTaxPerPerson"),
                            root.get("monthlyTaxPerPet"),
                            root.get("monthlyTaxPerM2"),
                            root.get("employee").get("name")
                    ))
                    .where(cb.and(
                            cb.equal(company.get("id"), companyId),
                            cb.isFalse(root.get("deleted"))
                    ));
            return session.createQuery(cr).getResultList();
        }
    }

    public static void updateEmployeeForBuilding(Session session, long buildingId, long newEmployeeId) {
        Employee employee = session.get(Employee.class, newEmployeeId);
        Building building = session.get(Building.class, buildingId);

        if (!building.isDeleted()) building.setEmployee(employee);
    }
}
