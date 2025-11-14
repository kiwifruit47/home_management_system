package org.cscb525.dto;

public class BuildingEmployeeDto {
    private String employeeName;
    private String buildingAddress;

    public BuildingEmployeeDto(String employeeName, String buildingAddress) {
        this.employeeName = employeeName;
        this.buildingAddress = buildingAddress;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    @Override
    public String toString() {
        return "EmployeeBuildingDto{" +
                "employeeName='" + employeeName + '\'' +
                ", buildingAddress='" + buildingAddress + '\'' +
                '}';
    }
}
