package org.cscb525.dto.apartment;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateApartmentDto {
    @NotNull
    private final int floor;
    @NotNull
    private final int apartmentNumber;
    @NotNull
    private final BigDecimal area;
    private final int pets;
    private final long buildingId;

    public CreateApartmentDto(int floor, int apartmentNumber, BigDecimal area, int pets, long buildingId) {
        this.floor = floor;
        this.apartmentNumber = apartmentNumber;
        this.area = area;
        this.pets = pets;
        this.buildingId = buildingId;
    }

    @NotNull
    public int getFloor() {
        return floor;
    }

    @NotNull
    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public @NotNull BigDecimal getArea() {
        return area;
    }

    public int getPets() {
        return pets;
    }

    public long getBuildingId() {
        return buildingId;
    }

    @Override
    public String toString() {
        return "CreateApartmentDto{" +
                "floor=" + floor +
                ", apartmentNumber=" + apartmentNumber +
                ", area=" + area +
                ", pets=" + pets +
                ", buildingId=" + buildingId +
                '}';
    }
}
