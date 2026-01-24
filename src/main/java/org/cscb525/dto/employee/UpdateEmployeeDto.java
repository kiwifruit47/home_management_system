package org.cscb525.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class UpdateEmployeeDto {
    @NotNull
    @Positive
    private long employeeId;
    @NotBlank(message = "Employee name cannot be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z ]*$", message = "Employee name has to start with capital letter and consist only of letters.")
    private String name;
    @NotNull
    @Positive
    private long companyId;

    public UpdateEmployeeDto(long employeeId, String name, long companyId) {
        this.employeeId = employeeId;
        this.name = name;
        this.companyId = companyId;
    }

    @NotNull
    @Positive
    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(@NotNull @Positive long employeeId) {
        this.employeeId = employeeId;
    }

    public @NotBlank(message = "Employee name cannot be blank") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Employee name cannot be blank") String name) {
        this.name = name;
    }

    @NotNull
    @Positive
    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(@NotNull @Positive long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "UpdateEmployeeDto{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}
