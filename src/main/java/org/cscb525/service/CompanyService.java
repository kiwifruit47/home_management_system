package org.cscb525.service;

import jakarta.validation.Valid;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.*;
import org.cscb525.dto.building.BuildingDto;
import org.cscb525.dto.building.CreateBuildingDto;
import org.cscb525.dto.company.CompanyDto;
import org.cscb525.dto.company.CompanyIncomeDto;
import org.cscb525.dto.company.CreateCompanyDto;
import org.cscb525.dto.employee.EmployeeDto;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;


public class CompanyService {
    public void createCompany(@Valid CreateCompanyDto createCompanyDto) {
        CompanyDao.createCompany(createCompanyDto);
    }

    public CompanyDto getCompanyById(long companyId) {
        return CompanyDao.findCompanyDtoById(companyId);
    }

    public List<CompanyDto> getAllCompanies() {
        return CompanyDao.findAllCompanies();
    }

    public List<CompanyIncomeDto> getCompaniesOrderedByIncome() {
        return CompanyDao.companiesOrderByIncomeDesc();
    }

    public CompanyDto changeCompanyName(long companyId, String name) {
        CompanyDao.updateCompanyName(companyId, name);
        return CompanyDao.findCompanyDtoById(companyId);
    }

    public EmployeeDto removeEmployeeFromCompany(long companyId, long employeeToRemoveId) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            List<BuildingDto> buildings = BuildingDao.findAllBuildingsByEmployee(employeeToRemoveId);

            List<Long> colleaguesIds = EmployeeDao.findAllColleaguesIdsOfEmployee(session, companyId, employeeToRemoveId);

            if (colleaguesIds.isEmpty())
                throw new IllegalStateException("Deleting last company employee is forbidden!");

            int employeeIndex = 0;
            for (BuildingDto buildingDto : buildings) {
                BuildingDao.updateEmployeeForBuilding(session, buildingDto.getId(), colleaguesIds.get(employeeIndex));
                employeeIndex = (employeeIndex + 1) % colleaguesIds.size();
            }

            session.flush();
            EmployeeDto deletedEmployeeDto = EmployeeDao.findEmployeeDtoById(session, employeeToRemoveId);
            EmployeeDao.deleteEmployee(session,employeeToRemoveId);

            transaction.commit();

            return deletedEmployeeDto;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void signNewContractForBuilding(long companyId, @Valid CreateBuildingRequest createBuildingRequest) {
        long employeeId = EmployeeDao.findEmployeeWithSmallestBuildingCountByCompany(companyId);
        CreateBuildingDto createBuildingDto = new CreateBuildingDto(
                createBuildingRequest.getAddress(),
                createBuildingRequest.getFloors(),
                createBuildingRequest.getMonthlyTaxPerPerson(),
                createBuildingRequest.getMonthlyTaxPerPet(),
                createBuildingRequest.getMonthlyTaxPerM2(),
                employeeId
        );

        BuildingDao.createBuilding(createBuildingDto);
    }

    public BuildingDto terminateContractAndDeleteBuilding(long buildingId) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            BuildingDto deletedBuildingDto = BuildingDao.findBuildingDtoById(session, buildingId);

            OwnerDao.deleteAllOwnersByBuilding(session, buildingId);
            OccupantDao.deleteAllOccupantsByBuilding(session, buildingId);
            ApartmentDao.deleteAllApartmentsByBuilding(session, buildingId);
            BuildingDao.deleteBuilding(session, buildingId);

            transaction.commit();

            return deletedBuildingDto;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public CompanyDto deleteCompany(long companyId) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            CompanyDto deletedCompanyDto = CompanyDao.findCompanyDtoById(session, companyId);

            OwnerDao.deleteAllOwnersByCompany(session, companyId);
            OccupantDao.deleteAllOccupantsByCompany(session,companyId);
            ApartmentDao.deleteAllApartmentsByCompany(session, companyId);
            BuildingDao.deleteAllBuildingsByCompany(session, companyId);
            EmployeeDao.deleteAllEmployeesByCompany(session, companyId);
            CompanyDao.deleteCompany(session, companyId);

            transaction.commit();

            return deletedCompanyDto;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public CompanyDto restoreCompany(long companyId) {
        CompanyDao.restoreCompany(companyId);
        return CompanyDao.findCompanyDtoById(companyId);
    }
}
