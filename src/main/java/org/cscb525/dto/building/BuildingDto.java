package org.cscb525.dto.building;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class BuildingDto {
    @NotNull
    private String address;
    @NotNull
    @Positive
    private int floors;
    @NotNull
    @Positive
    private BigDecimal monthlyTaxPerPerson;
    @NotNull
    @Positive
    private BigDecimal monthlyTaxPerPet;
    @NotNull
    @Positive
    private BigDecimal monthlyTaxPerM2;
    @NotBlank(message = "Employee name cannot be blank")
    private String employeeName;

    public BuildingDto(String address, int floors, BigDecimal monthlyTaxPerPerson, BigDecimal monthlyTaxPerPet, BigDecimal monthlyTaxPerM2, String employeeName) {
        this.address = address;
        this.floors = floors;
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
        this.monthlyTaxPerPet = monthlyTaxPerPet;
        this.monthlyTaxPerM2 = monthlyTaxPerM2;
        this.employeeName = employeeName;
    }

    public @NotNull String getAddress() {
        return address;
    }

    @NotNull
    @Positive
    public int getFloors() {
        return floors;
    }

    public @NotNull @Positive BigDecimal getMonthlyTaxPerPerson() {
        return monthlyTaxPerPerson;
    }

    public @NotNull @Positive BigDecimal getMonthlyTaxPerPet() {
        return monthlyTaxPerPet;
    }

    public @NotNull @Positive BigDecimal getMonthlyTaxPerM2() {
        return monthlyTaxPerM2;
    }

    public @NotBlank(message = "Employee name cannot be blank") String getEmployeeName() {
        return employeeName;
    }

    public void setAddress(@NotNull String address) {
        this.address = address;
    }

    public void setFloors(@NotNull @Positive int floors) {
        this.floors = floors;
    }

    public void setMonthlyTaxPerPerson(@NotNull @Positive BigDecimal monthlyTaxPerPerson) {
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
    }

    public void setMonthlyTaxPerPet(@NotNull @Positive BigDecimal monthlyTaxPerPet) {
        this.monthlyTaxPerPet = monthlyTaxPerPet;
    }

    public void setMonthlyTaxPerM2(@NotNull @Positive BigDecimal monthlyTaxPerM2) {
        this.monthlyTaxPerM2 = monthlyTaxPerM2;
    }

    public void setEmployeeName(@NotBlank(message = "Employee name cannot be blank") String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "BuildingDto{" +
                "address='" + address + '\'' +
                ", floors=" + floors +
                ", monthlyTaxPerPerson=" + monthlyTaxPerPerson +
                ", monthlyTaxPerPet=" + monthlyTaxPerPet +
                ", monthlyTaxPerM2=" + monthlyTaxPerM2 +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}
