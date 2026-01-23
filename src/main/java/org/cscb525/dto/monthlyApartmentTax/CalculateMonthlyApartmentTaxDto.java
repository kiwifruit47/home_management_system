package org.cscb525.dto.monthlyApartmentTax;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class CalculateMonthlyApartmentTaxDto {
    @NotNull
    private final BigDecimal area;
    @PositiveOrZero
    private final int pets;
    @PositiveOrZero
    private final int occupantCount;
    @Positive
    private final BigDecimal monthlyTaxPerPerson;
    @NotNull
    @Positive
    private final BigDecimal monthlyTaxPerPet;
    @NotNull
    @Positive
    private final BigDecimal monthlyTaxPerM2;

    public CalculateMonthlyApartmentTaxDto(BigDecimal area, int pets, int occupantCount, BigDecimal monthlyTaxPerPerson, BigDecimal monthlyTaxPerPet, BigDecimal monthlyTaxPerM2) {
        this.area = area;
        this.pets = pets;
        this.occupantCount = occupantCount;
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
        this.monthlyTaxPerPet = monthlyTaxPerPet;
        this.monthlyTaxPerM2 = monthlyTaxPerM2;
    }

    public @NotNull BigDecimal getArea() {
        return area;
    }

    @PositiveOrZero
    public int getPets() {
        return pets;
    }

    @PositiveOrZero
    public int getOccupantCount() {
        return occupantCount;
    }

    public @Positive BigDecimal getMonthlyTaxPerPerson() {
        return monthlyTaxPerPerson;
    }

    public @NotNull @Positive BigDecimal getMonthlyTaxPerPet() {
        return monthlyTaxPerPet;
    }

    public @NotNull @Positive BigDecimal getMonthlyTaxPerM2() {
        return monthlyTaxPerM2;
    }
}
