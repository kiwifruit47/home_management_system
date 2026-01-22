package org.cscb525.dto.building;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreateBuildingDto {
    @NotBlank(message = "Address cannot be blank")
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
    @NotNull
    private long employeeId;

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

    @NotNull
    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(@NotNull long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "CreateBuildingDto{" +
                "address='" + address + '\'' +
                ", floors=" + floors +
                ", monthlyTaxPerPerson=" + monthlyTaxPerPerson +
                ", monthlyTaxPerPet=" + monthlyTaxPerPet +
                ", monthlyTaxPerM2=" + monthlyTaxPerM2 +
                ", employeeId=" + employeeId +
                '}';
    }
}
