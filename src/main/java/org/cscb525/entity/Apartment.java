package org.cscb525.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "apartment")
public class Apartment extends BaseEntity {
    @NotNull
    private int floor;
    @NotNull
    private int apartment_number;
    @NotNull
    private BigDecimal area;
    private int pets;
    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;
    @ManyToMany(mappedBy = "apartments")
    private Set<Owner> owners;
    @OneToMany(mappedBy = "apartment")
    private Set<Occupant> occupants;
    @OneToMany(mappedBy = "apartment")
    @Column(name = "monthly_apartment_taxes")
    private Set<MonthlyApartmentTax> monthlyApartmentTaxes;

    public Apartment() {
    }

    public Apartment(int floor, int apartment_number, BigDecimal area, int pets, Building building, Set<Owner> owners, Set<Occupant> occupants) {
        this.floor = floor;
        this.apartment_number = apartment_number;
        this.area = area;
        this.pets = pets;
        this.building = building;
        this.owners = owners;
        this.occupants = occupants;
    }

    public Apartment(int floor, int apartment_number, BigDecimal area, int pets, Building building, Set<Owner> owners) {
        this.floor = floor;
        this.apartment_number = apartment_number;
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
    public int getApartment_number() {
        return apartment_number;
    }

    public void setApartment_number(@NotNull int apartment_number) {
        this.apartment_number = apartment_number;
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
                ", apartment_number=" + apartment_number +
                ", area=" + area +
                ", pets=" + pets +
                ", building=" + building +
                ", owners=" + owners +
                ", occupants=" + occupants +
                ", monthlyApartmentTaxes=" + monthlyApartmentTaxes +
                "} " + super.toString();
    }
}
