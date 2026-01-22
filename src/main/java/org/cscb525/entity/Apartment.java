package org.cscb525.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "apartment")
public class Apartment extends BaseEntity {
    @NotNull
    private int floor;
    @NotNull
    @Column(name = "apartment_number")
    private int apartmentNumber;
    @NotNull
    private BigDecimal area;
    @PositiveOrZero
    private int pets;
    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;
    @ManyToMany(mappedBy = "apartments")
    private Set<Owner> owners;
    @OneToMany(mappedBy = "apartment")
    private Set<Occupant> occupants;
    @OneToMany(mappedBy = "apartment")
    private Set<MonthlyApartmentTax> monthlyApartmentTaxes;

    public Apartment() {
    }

    public Apartment(int floor, int apartmentNumber, BigDecimal area, int pets, Building building, Set<Owner> owners, Set<Occupant> occupants) {
        this.floor = floor;
        this.apartmentNumber = apartmentNumber;
        this.area = area;
        this.pets = pets;
        this.building = building;
        this.owners = owners;
        this.occupants = occupants;
    }

    public Apartment(int floor, int apartmentNumber, BigDecimal area, int pets, Building building, Set<Owner> owners) {
        this.floor = floor;
        this.apartmentNumber = apartmentNumber;
        this.area = area;
        this.pets = pets;
        this.building = building;
        this.owners = owners;
    }

    @NotNull
    public int getFloor() {
        return floor;
    }

    public void setFloor(@NotNull int floor) {
        this.floor = floor;
    }

    @NotNull
    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(@NotNull int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public @NotNull BigDecimal getArea() {
        return area;
    }

    public void setArea(@NotNull BigDecimal area) {
        this.area = area;
    }

    public int getPets() {
        return pets;
    }

    public void setPets(int pets) {
        this.pets = pets;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Set<Owner> getOwners() {
        return owners;
    }

    public void setOwners(Set<Owner> owners) {
        this.owners = owners;
    }

    public Set<Occupant> getOccupants() {
        return occupants;
    }

    public void setOccupants(Set<Occupant> occupants) {
        this.occupants = occupants;
    }

    public Set<MonthlyApartmentTax> getMonthlyApartmentTaxes() {
        return monthlyApartmentTaxes;
    }

    public void setMonthlyApartmentTaxes(Set<MonthlyApartmentTax> monthlyApartmentTaxes) {
        this.monthlyApartmentTaxes = monthlyApartmentTaxes;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "floor=" + floor +
                ", apartmentNumber=" + apartmentNumber +
                ", area=" + area +
                ", pets=" + pets +
                ", building=" + building +
                ", owners=" + owners +
                ", occupants=" + occupants +
                ", monthlyApartmentTaxes=" + monthlyApartmentTaxes +
                "} " + super.toString();
    }
}
