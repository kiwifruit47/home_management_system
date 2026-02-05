package org.cscb525.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "occupant")
public class Occupant extends BaseEntity {
    @PositiveOrZero
    private int age;
    @NotBlank(message = "Occupant name cannot be blank")
    private String name;
    @Column(name = "uses_elevator")
    private boolean usesElevator = true;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

    public Occupant() {
    }

    public Occupant(int age, String name, boolean usesElevator, Apartment apartment) {
        this.age = age;
        this.name = name;
        this.usesElevator = usesElevator;
        this.apartment = apartment;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUsesElevator() {
        return usesElevator;
    }

    public void setUsesElevator(boolean usesElevator) {
        this.usesElevator = usesElevator;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @Override
    public String toString() {
        return "Occupant{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", usesElevator=" + usesElevator +
                "} " + super.toString();
    }
}
