package org.cscb525.dto.occupant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class CreateOccupantDto {
    @PositiveOrZero
    private final int age;
    @NotBlank(message = "Occupant name cannot be blank")
    private final String name;
    private final boolean usesElevator;
    @NotNull
    private final int apartmentId;

    public CreateOccupantDto(int age, String name, boolean usesElevator, int apartmentId) {
        this.age = age;
        this.name = name;
        this.usesElevator = usesElevator;
        this.apartmentId = apartmentId;
    }

    @NotNull
    @PositiveOrZero
    public int getAge() {
        return age;
    }

    public @NotBlank(message = "Occupant name cannot be blank") String getName() {
        return name;
    }

    @NotNull
    public boolean usesElevator() {
        return usesElevator;
    }

    @NotNull
    public int getApartmentId() {
        return apartmentId;
    }

    @Override
    public String toString() {
        return "CreateOccupantDto{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", usesElevator=" + usesElevator +
                ", apartmentId=" + apartmentId +
                '}';
    }
}
