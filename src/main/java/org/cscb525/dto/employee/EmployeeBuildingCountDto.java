package org.cscb525.dto.employee;

import jakarta.validation.constraints.*;

public class EmployeeBuildingCountDto {
    @NotBlank(message = "Employee name cannot be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z ]*$", message = "Employee name has to start with capital letter and consist only of letters.")
    private final String name;
    @PositiveOrZero
    private final int buildings;

    public EmployeeBuildingCountDto(String name, int buildings) {
        this.name = name;
        this.buildings = buildings;
    }

    public String getName() {
        return name;
    }

    public int getBuildings() {
        return buildings;
    }

    @Override
    public String toString() {
        return "EmployeeBuildingsDto{" +
                "name=" + name +
                ", buildings=" + buildings +
                '}';
    }
}
