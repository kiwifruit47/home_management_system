package org.cscb525.dto;

import java.util.List;

public class EmployeeBuildingsDto {
    private EmployeeDto employee;
    private List<BuildingDto> buildings;

    public EmployeeBuildingsDto(EmployeeDto employee, List<BuildingDto> buildings) {
        this.employee = employee;
        this.buildings = buildings;
    }

    public EmployeeDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDto employee) {
        this.employee = employee;
    }

    public List<BuildingDto> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingDto> buildings) {
        this.buildings = buildings;
    }

    @Override
    public String toString() {
        return "EmployeeBuildingsDto{" +
                "employee=" + employee +
                ", buildings=" + buildings +
                '}';
    }
}
