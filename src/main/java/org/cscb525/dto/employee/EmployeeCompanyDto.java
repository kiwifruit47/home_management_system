package org.cscb525.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EmployeeCompanyDto {
    @NotBlank(message = "Company name cannot be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Company name must start with a capital letter and consist only of letters")
    private String companyName;
    @NotBlank(message = "Employee name cannot be blank")
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
