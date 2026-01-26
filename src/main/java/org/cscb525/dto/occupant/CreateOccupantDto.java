package org.cscb525.dto.occupant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class CreateOccupantDto {
    @PositiveOrZero
    private final int age;
    @NotBlank(message = "Occupant name cannot be blank")
    private final String name;
    private final boolean usesElevator;

    public CreateOccupantDto(int age, String name, boolean usesElevator) {
        this.age = age;
        this.name = name;
        this.usesElevator = usesElevator;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public boolean usesElevator() {
        return usesElevator;
    }

    @Override
    public String toString() {
        return "CreateOccupantDto{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", usesElevator=" + usesElevator +
                '}';
    }
}
