package org.cscb525.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateEmployeeDto {
    @NotBlank(message = "Employee name cannot be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z ]*$", message = "Employee name has to start with capital letter and consist only of letters.")
    private String name;
    @NotNull
    private long companyId;

    public CreateEmployeeDto(String name, long companyId) {
        this.name = name;
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "CreateEmployeeDto{" +
                "name='" + name + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}
