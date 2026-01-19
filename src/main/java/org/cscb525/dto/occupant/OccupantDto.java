package org.cscb525.dto.occupant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class OccupantDto {
    @NotBlank(message = "Occupant name cannot be blank")
    private final String name;
    @PositiveOrZero
    private final int age;
    @Positive
    private final int apartmentNumber;
    @Positive
    private final int floor;

    public OccupantDto(String name, int age, int apartmentNumber, int floor) {
        this.name = name;
        this.age = age;
        this.apartmentNumber = apartmentNumber;
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public int getFloor() {
        return floor;
    }

    @Override
    public String toString() {
        return "OccupantDto{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", apartmentNumber=" + apartmentNumber +
                ", floor=" + floor +
                '}';
    }
}
