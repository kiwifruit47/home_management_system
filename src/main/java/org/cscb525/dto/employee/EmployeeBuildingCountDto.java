package org.cscb525.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class EmployeeBuildingCountDto {
    @NotBlank(message = "Employee name cannot be blank")
    private final String name;
    @PositiveOrZero
    private final int buildings;

    public EmployeeBuildingCountDto(String name, int buildings) {
        this.name = name;
        this.buildings = buildings;
    }

    public @NotBlank(message = "Employee name cannot be blank") String getName() {
        return name;
    }

    @PositiveOrZero
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
