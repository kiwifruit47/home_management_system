package service;

import org.cscb525.dto.building.BuildingDto;
import org.cscb525.dto.building.UpdateBuildingDto;
import org.cscb525.exceptions.EmptyListException;
import org.cscb525.service.BuildingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BuildingServiceIT {
    private final BuildingService buildingService = new BuildingService();
    @BeforeEach
    void setup() {
        TestDataSetup.setupData();
    }

    @AfterEach
    void cleanup() {
        TestDataSetup.clearDB();
    }

    @Test
    public void updateBuilding_validData_updatesAndReturnsDto() {
        UpdateBuildingDto dto = new UpdateBuildingDto(
                1L,
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        BuildingDto result = buildingService.updateBuilding(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(0, BigDecimal.ONE.compareTo(result.getMonthlyTaxPerPerson()));
        assertEquals(0, BigDecimal.ONE.compareTo(result.getMonthlyTaxPerPet()));
        assertEquals(0, BigDecimal.ONE.compareTo(result.getMonthlyTaxPerM2()));
    }

    @Test
    public void getBuildingById_existingBuilding_returnsDto() {
        BuildingDto building = buildingService.getBuildingById(1L);

        assertNotNull(building);
        assertEquals(1L, building.getId());
    }

    @Test
    public void getBuildingById_nonExistingBuilding_throwsException() {
        assertThrows(
                RuntimeException.class,
                () -> buildingService.getBuildingById(999L)
        );
    }

    @Test
    public void getAllBuildings_existingBuildings_returnsList() {
        List<BuildingDto> buildings = buildingService.getAllBuildings();

        assertNotNull(buildings);
        assertFalse(buildings.isEmpty());
    }

    @Test
    public void getAllBuildings_noBuildings_throwsEmptyListException() {
        TestDataSetup.clearDB();

        assertThrows(
                EmptyListException.class,
                buildingService::getAllBuildings
        );
    }

    @Test
    public void getBuildingCountOfCompany_companyWithBuildings_returnsCorrectCount() {
        int count = buildingService.getBuildingCountOfCompany(1L);

        assertTrue(count > 0);
    }

    @Test
    public void getBuildingCountOfCompany_companyWithoutBuildings_returnsZero() {
        int count = buildingService.getBuildingCountOfCompany(999L);

        assertEquals(0, count);
    }

    @Test
    public void getAllBuildingsByEmployee_employeeWithBuildings_returnsList() {
        List<BuildingDto> buildings =
                buildingService.getAllBuildingsByEmployee(1L);

        assertNotNull(buildings);
        assertFalse(buildings.isEmpty());
    }

    @Test
    public void getAllBuildingsByEmployee_employeeWithoutBuildings_throwsException() {
        assertThrows(
                EmptyListException.class,
                () -> buildingService.getAllBuildingsByEmployee(999L)
        );
    }

    @Test
    public void getAllBuildingsByCompany_companyWithBuildings_returnsList() {
        List<BuildingDto> buildings =
                buildingService.getAllBuildingsByCompany(1L);

        assertNotNull(buildings);
        assertFalse(buildings.isEmpty());
    }

    @Test
    public void getAllBuildingsByCompany_companyWithoutBuildings_throwsException() {
        assertThrows(
                EmptyListException.class,
                () -> buildingService.getAllBuildingsByCompany(999L)
        );
    }
}
