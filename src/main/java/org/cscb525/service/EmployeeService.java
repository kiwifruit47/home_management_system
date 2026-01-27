package org.cscb525.service;

import org.cscb525.dao.EmployeeDao;
import org.cscb525.dto.employee.CreateEmployeeDto;
import org.cscb525.dto.employee.EmployeeBuildingCountDto;
import org.cscb525.dto.employee.EmployeeDto;
import org.cscb525.entity.Employee;
import org.cscb525.exceptions.EmptyListException;

import java.util.List;

public class EmployeeService {
    public void createEmployee(CreateEmployeeDto employeeDto) {
        EmployeeDao.createEmployee(employeeDto);
    }

    public EmployeeDto updateEmployeeName(long id, String name) {
        EmployeeDao.updateEmployeeName(id, name);
        return EmployeeDao.findEmployeeDtoById(id);
    }

    public EmployeeDto getEmployeeById(long id) {
        return EmployeeDao.findEmployeeDtoById(id);
    }

    public List<EmployeeDto> getAllEmployees() {
        List<EmployeeDto> employees = EmployeeDao.findAllEmployees();
        if (employees.isEmpty()) throw new EmptyListException(Employee.class);
        return employees;
    }

    public List<EmployeeDto> getAllEmployeesByCompany(long companyId) {
        List<EmployeeDto> employees = EmployeeDao.findAllEmployeesByCompany(companyId);
        if (employees.isEmpty()) throw new EmptyListException(Employee.class);
        return employees;
    }

    public List<EmployeeBuildingCountDto> getEmployeesOrderedByBuildingCountAndNameByCompany(long companyId) {
        List<EmployeeBuildingCountDto> employees = EmployeeDao.findEmployeesGroupByCompanyOrderByBuildingCountDescAndNameAsc(companyId);
        if (employees.isEmpty()) throw new EmptyListException(Employee.class);
        return employees;
    }
}
