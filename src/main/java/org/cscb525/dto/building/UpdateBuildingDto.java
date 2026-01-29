package org.cscb525.dto.building;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class UpdateBuildingDto {
    @Positive
    private final long id;
    @NotNull
    @Positive
    private final BigDecimal monthlyTaxPerPerson;
    @NotNull
    @Positive
    private final BigDecimal monthlyTaxPerPet;
    @NotNull
    @Positive
    private final BigDecimal monthlyTaxPerM2;

    public UpdateBuildingDto(long id, BigDecimal monthlyTaxPerPerson, BigDecimal monthlyTaxPerPet, BigDecimal monthlyTaxPerM2) {
        this.id = id;
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
        this.monthlyTaxPerPet = monthlyTaxPerPet;
        this.monthlyTaxPerM2 = monthlyTaxPerM2;
    }

    public long getId() {
        return id;
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


    @Override
    public String toString() {
        return "UpdateBuildingDto{" +
                "id=" + id +
                ", monthlyTaxPerPerson=" + monthlyTaxPerPerson +
                ", monthlyTaxPerPet=" + monthlyTaxPerPet +
                ", monthlyTaxPerM2=" + monthlyTaxPerM2 +
                '}';
    }
}
