package org.cscb525.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class CreateBuildingRequest {
    @NotBlank(message = "Address cannot be blank")
    private final String address;
    @NotNull
    @Positive
    private final int floors;
    @NotNull
    @Positive
    private final BigDecimal monthlyTaxPerPerson;
    @NotNull
    @Positive
    private final BigDecimal monthlyTaxPerPet;
    @NotNull
    @Positive
    private final BigDecimal monthlyTaxPerM2;

    public CreateBuildingRequest(String address, int floors, BigDecimal monthlyTaxPerPerson, BigDecimal monthlyTaxPerPet, BigDecimal monthlyTaxPerM2) {
        this.address = address;
        this.floors = floors;
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
        this.monthlyTaxPerPet = monthlyTaxPerPet;
        this.monthlyTaxPerM2 = monthlyTaxPerM2;
    }

    public String getAddress() {
        return address;
    }

    public int getFloors() {
        return floors;
    }

    public BigDecimal getMonthlyTaxPerPerson() {
        return monthlyTaxPerPerson;
    }

    public BigDecimal getMonthlyTaxPerPet() {
        return monthlyTaxPerPet;
    }

    public BigDecimal getMonthlyTaxPerM2() {
        return monthlyTaxPerM2;
    }
}
