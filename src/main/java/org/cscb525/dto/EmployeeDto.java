package org.cscb525.dto;

import jakarta.validation.constraints.NotBlank;

public class EmployeeDto {
    @NotBlank(message = "Employee name cannot be blank")
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
