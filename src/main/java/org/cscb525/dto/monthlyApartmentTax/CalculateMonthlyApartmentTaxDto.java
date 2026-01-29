package org.cscb525.dto.monthlyApartmentTax;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class CalculateMonthlyApartmentTaxDto {
    @NotNull
    @Positive
    private final BigDecimal area;
    @PositiveOrZero
    private final int pets;
    @PositiveOrZero
    //above 7 years old && use elevator
    private final long taxedOccupantCount;
    @Positive
    private final BigDecimal monthlyTaxPerPerson;
    @NotNull
    @Positive
    private final BigDecimal monthlyTaxPerPet;
    @NotNull
    @Positive
    private final BigDecimal monthlyTaxPerM2;

    public CalculateMonthlyApartmentTaxDto(BigDecimal area, int pets, long occupantCount, BigDecimal monthlyTaxPerPerson, BigDecimal monthlyTaxPerPet, BigDecimal monthlyTaxPerM2) {
        this.area = area;
        this.pets = pets;
        this.taxedOccupantCount = occupantCount;
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
        this.monthlyTaxPerPet = monthlyTaxPerPet;
        this.monthlyTaxPerM2 = monthlyTaxPerM2;
    }

    public BigDecimal getArea() {
        return area;
    }

    public int getPets() {
        return pets;
    }

    public long getTaxedOccupantCount() {
        return taxedOccupantCount;
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
