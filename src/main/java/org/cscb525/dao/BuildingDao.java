package org.cscb525.dao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.building.BuildingDto;
import org.cscb525.dto.building.CreateBuildingDto;
import org.cscb525.dto.building.UpdateBuildingDto;
import org.cscb525.dto.employee.EmployeeDto;
import org.cscb525.entity.Building;
import org.cscb525.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BuildingDao {
    public static void createBuilding(CreateBuildingDto buildingDto) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, buildingDto.getEmployeeId());

            if (employee == null) {
                throw new EntityNotFoundException("No employee with id " + buildingDto.getEmployeeId() + " found.");
            }

            Building building = new Building();
            building.setAddress(buildingDto.getAddress());
            building.setEmployee(employee);
            building.setFloors(buildingDto.getFloors());
            building.setMonthlyTaxPerPerson(buildingDto.getMonthlyTaxPerPerson());
            building.setMonthlyTaxPerM2(buildingDto.getMonthlyTaxPerM2());
            building.setMonthlyTaxPerPet(buildingDto.getMonthlyTaxPerPet());

            session.persist(building );

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void updateBuilding(UpdateBuildingDto buildingDto) {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Building building = session.get(Building.class, buildingDto.getId());
            if (building == null) {
                throw new EntityNotFoundException(
                        "No building with id " + buildingDto.getId() + " found."
                );
            }

            Employee employee = session.get(Employee.class, buildingDto.getEmployeeId());
            if (employee == null) {
                throw new EntityNotFoundException(
                        "No employee with id " + buildingDto.getEmployeeId() + " found."
                );
            }

            building.setFloors(buildingDto.getFloors());
            building.setMonthlyTaxPerPerson(buildingDto.getMonthlyTaxPerPerson());
            building.setMonthlyTaxPerM2(buildingDto.getMonthlyTaxPerM2());
            building.setMonthlyTaxPerPet(buildingDto.getMonthlyTaxPerPet());
            building.setEmployee(employee);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public static BuildingDto findBuildingById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BuildingDto> cr = cb.createQuery(BuildingDto.class);
            Root<Building> root = cr.from(Building.class);

            cr.select(cb.construct(
                    BuildingDto.class,
                    root.get("address"),
                    root.get("floors"),
                    root.get("monthlyTaxPerPerson"),
                    root.get("monthlyTaxPerPet"),
                    root.get("monthlyTaxPerM2"),
                    root.get("employee").get("name")
            ))
                    .where(cb.equal(root.get("id"), id));
            return session.createQuery(cr).getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No building with id " + id + " found.");
        }
    }
    public static List<BuildingDto> findAllBuildings() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BuildingDto> cr = cb.createQuery(BuildingDto.class);
            Root<Building> root = cr.from(Building.class);

            cr.select(cb.construct(
                            BuildingDto.class,
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

    public static long findAllBuildingCountByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<Building> root = cr.from(Building.class);

            Join<?, ?> company = root.join("company");

            cr.select(cb.construct(
                            Long.class,
                            cb.count(root)
                    ))
                    .where(cb.equal(company.get("id"), companyId));

            return session.createQuery(cr).getSingleResult();
        }
    }

    public static List<BuildingDto> findAllBuildingsByEmployee(long employeeId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BuildingDto> cr = cb.createQuery(BuildingDto.class);
            Root<Building> root = cr.from(Building.class);

            Join<?, ?> employee = root.join("employee");

            cr.select(cb.construct(
                            BuildingDto.class,
                            root.get("address")
                    ))
                    .where(cb.equal(employee.get("id"), employeeId));
            return session.createQuery(cr).getResultList();
        }
    }

    public static  List<BuildingDto> getAllBuildingsByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BuildingDto> cr = cb.createQuery(BuildingDto.class);
            Root<Building> root = cr.from(Building.class);

            Join<?, ?> employee = root.join("employee");
            Join<?, ?> company = employee.join("company");

            cr.select(cb.construct(
                            BuildingDto.class,
                            root.get("address")
                    ))
                    .where(cb.equal(company.get("id"), companyId));
            return session.createQuery(cr).getResultList();
        }
    }
}
