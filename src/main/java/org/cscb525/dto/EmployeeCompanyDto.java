package org.cscb525.dto;

public class EmployeeCompanyDto {
    private String companyName;
    private String employeeName;

    public EmployeeCompanyDto(String companyName, String employeeName) {
        this.companyName = companyName;
        this.employeeName = employeeName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "EmployeeCompanyDto{" +
                "companyName='" + companyName + '\'' +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}
