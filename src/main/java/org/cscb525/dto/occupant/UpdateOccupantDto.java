package org.cscb525.dto.occupant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class UpdateOccupantDto {
    private final long id;
    @PositiveOrZero
    private final int age;
    @NotBlank(message = "Occupant name cannot be blank")
    private final String name;
    @NotNull
    private final boolean usesElevator;

    public UpdateOccupantDto(long id, int age, String name, boolean usesElevator) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.usesElevator = usesElevator;
    }

    @NotNull
    public long getId() {
        return id;
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
    public boolean isUsesElevator() {
        return usesElevator;
    }

    @Override
    public String toString() {
        return "UpdateOccupantDto{" +
                "id=" + id +
                "age=" + age +
                ", name='" + name + '\'' +
                ", usesElevator=" + usesElevator +
                '}';
    }
}
