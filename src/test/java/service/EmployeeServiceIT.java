package service;

import org.cscb525.dto.employee.CreateEmployeeDto;
import org.cscb525.dto.employee.EmployeeBuildingCountDto;
import org.cscb525.dto.employee.EmployeeDto;
import org.cscb525.exceptions.EmptyListException;
import org.cscb525.exceptions.NotFoundException;
import org.cscb525.service.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceIT {
    private final EmployeeService employeeService = new EmployeeService();
    @BeforeEach
    void setup() {
        TestDataSetup.setupData();
    }

    @AfterEach
    void cleanup() {
        TestDataSetup.clearDB();
    }

    @Test
    public void createEmployee_success() {
        CreateEmployeeDto dto = new CreateEmployeeDto("New Employee", 1);

        employeeService.createEmployee(dto);

        List<EmployeeDto> employees = employeeService.getAllEmployees();
        assertTrue(
                employees.stream().anyMatch(e -> e.getName().equals("New Employee"))
        );
    }

    @Test
    public void createEmployee_companyNotFound_throwsException() {
        CreateEmployeeDto dto = new CreateEmployeeDto("Ghost", 999);

        assertThrows(
                NotFoundException.class,
                () -> employeeService.createEmployee(dto)
        );
    }

    @Test
    public void updateEmployeeName_success() {
        EmployeeDto updated =
                employeeService.updateEmployeeName(1L, "Updated Name");

        assertEquals("Updated Name", updated.getName());
    }

    @Test
    public void updateEmployeeName_employeeNotFound_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> employeeService.updateEmployeeName(999L, "Nope")
        );
    }

    @Test
    void getEmployeeById_success() {
        EmployeeDto employee = employeeService.getEmployeeById(1L);

        assertNotNull(employee);
        assertEquals("Sofia", employee.getName());
    }

    @Test
    void getEmployeeById_notFound_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> employeeService.getEmployeeById(999L)
        );
    }

    @Test
    public void getAllEmployees_success() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();

        assertFalse(employees.isEmpty());
        assertTrue(employees.size() == 6);
    }

    @Test
    public void getAllEmployees_empty_throwsException() {
        TestDataSetup.clearDB();

        assertThrows(
                EmptyListException.class,
                employeeService::getAllEmployees
        );
    }

    @Test
    public void getAllEmployeesByCompany_success() {
        List<EmployeeDto> employees =
                employeeService.getAllEmployeesByCompany(1L);

        assertEquals(3, employees.size());
        assertEquals("Sofia", employees.getFirst().getName());
        assertEquals("Alexander", employees.get(1).getName());
        assertEquals("Maria", employees.get(2).getName());
    }

    @Test
    public void getAllEmployeesByCompany_noEmployees_throwsException() {
        assertThrows(
                EmptyListException.class,
                () -> employeeService.getAllEmployeesByCompany(999L)
        );
    }

    @Test
    public void getEmployeesOrderedByBuildingCountAndName_success() {
        List<EmployeeBuildingCountDto> result =
                employeeService.getEmployeesOrderedByBuildingCountAndNameByCompany(1L);

        assertFalse(result.isEmpty());

        for (int i = 1; i < result.size(); i++) {
            EmployeeBuildingCountDto previous = result.get(i - 1);
            EmployeeBuildingCountDto current = result.get(i);

            assertTrue(
                    previous.getBuildings() >= current.getBuildings()
                            || (previous.getBuildings() == current.getBuildings()
                            && previous.getName().compareTo(current.getName()) <= 0)
            );
        }
    }

    @Test
    public void getEmployeesOrderedByBuildingCountAndName_empty_throwsException() {
        assertThrows(
                EmptyListException.class,
                () -> employeeService.getEmployeesOrderedByBuildingCountAndNameByCompany(999L)
        );
    }

}
