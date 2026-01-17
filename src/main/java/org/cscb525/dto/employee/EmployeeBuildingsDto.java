package org.cscb525.dto.employee;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.cscb525.dto.BuildingDto;

import java.util.List;

public class EmployeeBuildingsDto {
    @NotNull
    private String name;
    @Positive
    private int buildings;

    public EmployeeBuildingsDto(String name, int buildings) {
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
