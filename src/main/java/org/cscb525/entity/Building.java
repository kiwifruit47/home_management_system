package org.cscb525.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "building")
public class Building extends BaseEntity {
    @NotBlank(message = "Address cannot be blank")
    private String address;
    @Positive
    private int floors;
    @NotNull
    @Positive
    @Column(name = "monthly_tax_per_person")
    private BigDecimal monthlyTaxPerPerson;
    @NotNull
    @Positive
    @Column(name = "monthly_tax_per_pet")
    private BigDecimal monthlyTaxPerPet;
    @NotNull
    @Positive
    @Column(name = "monthly_tax_per_m2")
    private BigDecimal monthlyTaxPerM2;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;
    @OneToMany(mappedBy = "building")
    private Set<Apartment> apartments = new HashSet<>();

    public Building() {
    }

    public Building(String address, int floors, BigDecimal monthlyTaxPerPerson, BigDecimal monthlyTaxPerPet, BigDecimal monthlyTaxPerM2, Employee employee) {
        this.address = address;
        this.floors = floors;
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
        this.monthlyTaxPerPet = monthlyTaxPerPet;
        this.monthlyTaxPerM2 = monthlyTaxPerM2;
        this.employee = employee;
    }

    public @NotBlank(message = "Address cannot be blank") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "Address cannot be blank") String address) {
        this.address = address;
    }

    @NotNull
    @Positive
    public int getFloors() {
        return floors;
    }

    public void setFloors(@NotNull @Positive int floors) {
        this.floors = floors;
    }

    public @NotNull @Positive BigDecimal getMonthlyTaxPerPerson() {
        return monthlyTaxPerPerson;
    }

    public void setMonthlyTaxPerPerson(@NotNull @Positive BigDecimal monthlyTaxPerPerson) {
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
    }

    public @NotNull @Positive BigDecimal getMonthlyTaxPerPet() {
        return monthlyTaxPerPet;
    }

    public void setMonthlyTaxPerPet(@NotNull @Positive BigDecimal monthlyTaxPerPet) {
        this.monthlyTaxPerPet = monthlyTaxPerPet;
    }

    public @NotNull @Positive BigDecimal getMonthlyTaxPerM2() {
        return monthlyTaxPerM2;
    }

    public void setMonthlyTaxPerM2(@NotNull @Positive BigDecimal monthlyTaxPerM2) {
        this.monthlyTaxPerM2 = monthlyTaxPerM2;
    }

    public @NotNull Employee getEmployee() {
        return employee;
    }

    public void setEmployee(@NotNull Employee employee) {
        this.employee = employee;
    }

    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }

    @Override
    public String toString() {
        return "Building{" +
                "address='" + address + '\'' +
                ", floors=" + floors +
                ", monthlyTaxPerPerson=" + monthlyTaxPerPerson +
                ", monthlyTaxPerPet=" + monthlyTaxPerPet +
                ", monthlyTaxPerM2=" + monthlyTaxPerM2 +
                "} " + super.toString();
    }
}
