package org.cscb525.service;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.*;
import org.cscb525.dto.building.CreateBuildingDto;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class CompanyService {
    public void deleteCompany(long companyId) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            OwnerDao.deleteAllOwnersByCompany(session, companyId);
            OccupantDao.deleteAllOccupantsByCompany(session,companyId);
            ApartmentDao.deleteAllApartmentsByCompany(session, companyId);
            BuildingDao.deleteAllBuildingsByCompany(session, companyId);
            EmployeeDao.deleteAllEmployeesByCompany(session, companyId);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void removeEmployeeFromCompany(long companyId, long employeeId) {
        //open session and begin transaction
        //get all employee count for c excluding employee to be deleted
        //append building to employee with least amount of buildings
        //remove employee
        //commit transaction
        //catch runtime exception
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();



            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void signNewContractForBuilding(long companyId, CreateBuildingRequest createBuildingRequest) {
        long employeeId = EmployeeDao.findEmployeeWithSmallestBuildingCountByCompany(companyId);
        CreateBuildingDto createBuildingDto = new CreateBuildingDto(
                createBuildingRequest.getAddress(),
                createBuildingRequest.getFloors(),
                createBuildingRequest.getMonthlyTaxPerPerson(),
                createBuildingRequest.getMonthlyTaxPerPet(),
                createBuildingRequest.getMonthlyTaxPerM2(),
                employeeId
        );
    }

    public void terminateContractAndDeleteBuilding(long buildingId) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            OwnerDao.deleteAllOwnersByBuilding(session, buildingId);
            OccupantDao.deleteAllOccupantsByBuilding(session, buildingId);
            ApartmentDao.deleteAllApartmentsByBuilding(session, buildingId);
            BuildingDao.deleteBuilding(session, buildingId);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
