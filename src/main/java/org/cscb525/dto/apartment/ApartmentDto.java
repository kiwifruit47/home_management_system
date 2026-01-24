package org.cscb525.dto.apartment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class ApartmentDto {
    @NotNull
    @Positive
    private final int floor;
    @NotNull
    private final int apartmentNumber;
    @NotNull
    private final BigDecimal area;
    @PositiveOrZero
    private final int pets;
    @NotBlank(message = "Address cannot be blank")
    private final String buildingAddress;

    public ApartmentDto(int floor, int apartmentNumber, BigDecimal area, int pets, String buildingAddress) {
        this.floor = floor;
        this.apartmentNumber = apartmentNumber;
        this.area = area;
        this.pets = pets;
        this.buildingAddress = buildingAddress;
    }

    public int getFloor() {
        return floor;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public BigDecimal getArea() {
        return area;
    }

    public int getPets() {
        return pets;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    @Override
    public String toString() {
        return "ApartmentDto{" +
                "floor=" + floor +
                ", apartmentNumber=" + apartmentNumber +
                ", area=" + area +
                ", pets=" + pets +
                ", buildingAddress='" + buildingAddress + '\'' +
                '}';
    }
}
