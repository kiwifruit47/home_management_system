package org.cscb525.dto;

import java.math.BigDecimal;

public class BuildingDto {
    private String address;
    private int floors;
    private BigDecimal monthlyTaxPerPerson;
    private BigDecimal monthlyTaxPerPet;

    public BuildingDto(String address, int floors, BigDecimal monthlyTaxPerPerson, BigDecimal monthlyTaxPerPet) {
        this.address = address;
        this.floors = floors;
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
        this.monthlyTaxPerPet = monthlyTaxPerPet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public BigDecimal getMonthlyTaxPerPerson() {
        return monthlyTaxPerPerson;
    }

    public void setMonthlyTaxPerPerson(BigDecimal monthlyTaxPerPerson) {
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
    }

    public BigDecimal getMonthlyTaxPerPet() {
        return monthlyTaxPerPet;
    }

    public void setMonthlyTaxPerPet(BigDecimal monthlyTaxPerPet) {
        this.monthlyTaxPerPet = monthlyTaxPerPet;
    }

    @Override
    public String toString() {
        return "BuildingDto{" +
                "address='" + address + '\'' +
                ", floors=" + floors +
                ", monthlyTaxPerPerson=" + monthlyTaxPerPerson +
                ", monthlyTaxPerPet=" + monthlyTaxPerPet +
                '}';
    }
}
