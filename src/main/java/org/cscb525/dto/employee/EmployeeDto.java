package org.cscb525.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EmployeeDto {
    @NotBlank(message = "Employee name cannot be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z ]*$", message = "Employee name has to start with capital letter and consist only of letters.")
    private String name;

    public EmployeeDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
