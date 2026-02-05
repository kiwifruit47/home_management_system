package service;

import org.cscb525.dao.BuildingDao;
import org.cscb525.dao.EmployeeDao;
import org.cscb525.dto.building.BuildingDto;
import org.cscb525.dto.company.CompanyDto;
import org.cscb525.dto.company.CompanyIncomeDto;
import org.cscb525.dto.employee.EmployeeDto;
import org.cscb525.exceptions.NotFoundException;
import org.cscb525.service.*;
import org.cscb525.dto.company.CreateCompanyDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyServiceIT {

    private CompanyService companyService = new CompanyService();

    @BeforeEach
    void setup() {
        TestDataSetup.setupData();
    }

    @AfterEach
    void cleanup() {
        TestDataSetup.clearDB();
    }

    @Test
    public void createCompany_validDto_companyIsCreated() {
        CreateCompanyDto dto = new CreateCompanyDto("New Company");

        companyService.createCompany(dto);

        CompanyDto company = companyService.getCompanyById(4);

        assertEquals("New Company", company.getName());
    }

    @Test
    public void getCompanyById_existingCompany_returnsCompany() {
        CompanyDto company = companyService.getCompanyById(1);

        assertEquals("Total Building Management", company.getName());
    }

    @Test
    public void getCompanyById_missingCompany_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> companyService.getCompanyById(999)
        );
    }

    @Test
    public void getAllCompanies_returnsAllCompanies() {
        List<CompanyDto> companies = companyService.getAllCompanies();

        assertEquals(3, companies.size());
    }

    @Test
    public void changeCompanyName_validCompany_updatesName() {
        CompanyDto updated =
                companyService.changeCompanyName(1, "Updated Name");

        assertEquals("Updated Name", updated.getName());
    }

    @Test
    public void getCompaniesOrderedByIncome_returnsSortedDescending() {
        List<CompanyIncomeDto> result =
                companyService.getCompaniesOrderedByIncome();

        assertFalse(result.isEmpty());

        for (int i = 0; i < result.size() - 1; i++) {
            BigDecimal current = result.get(i).getIncome();
            BigDecimal next = result.get(i + 1).getIncome();

            assertTrue(current.compareTo(next) >= 0);
        }
    }

    @Test
    public void removeEmployeeFromCompany_validEmployee_removesEmployee() {
        EmployeeDto removed =
                companyService.removeEmployeeFromCompany(1, 1);

        assertEquals("Sofia", removed.getName());

        assertThrows(
                NotFoundException.class,
                () -> EmployeeDao.findEmployeeDtoById(1)
        );
    }

    @Test
    public void removeEmployeeFromCompany_lastEmployee_throwsException() {
        assertThrows(
                IllegalStateException.class,
                () -> companyService.removeEmployeeFromCompany(3, 6)
        );
    }

    @Test
    public void signNewContractForBuilding_assignsEmployeeAutomatically() {
        CreateBuildingRequest request =
                new CreateBuildingRequest(
                        "Test Address",
                        3,
                        BigDecimal.ONE,
                        BigDecimal.ONE,
                        BigDecimal.ONE
                );

        companyService.signNewContractForBuilding(1, request);

        List<BuildingDto> buildings =
                BuildingDao.findAllBuildingsByCompany(1);

        assertTrue(
                buildings.stream()
                        .anyMatch(b -> b.getAddress().equals("Test Address"))
        );
    }


    @Test
    public void terminateContractAndDeleteBuilding_deletesEverything() {
        BuildingDto deleted =
                companyService.terminateContractAndDeleteBuilding(1);

        assertEquals(1, deleted.getId());

        assertThrows(
                NotFoundException.class,
                () -> BuildingDao.findBuildingDtoById(1)
        );
    }

    @Test
    public void deleteCompany_deletesCompanyAndRelations() {
        CompanyDto deleted = companyService.deleteCompany(1);

        assertEquals("Total Building Management", deleted.getName());

        assertThrows(
                NotFoundException.class,
                () -> companyService.getCompanyById(1)
        );
    }

    @Test
    public void restoreCompany_restoresAllEntities() {
        companyService.deleteCompany(1);

        CompanyDto restored =
                companyService.restoreCompany(1);

        assertEquals("Total Building Management", restored.getName());
    }
}
