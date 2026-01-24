package org.cscb525.dto.building;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class UpdateBuildingDto {
    @Positive
    private final long id;
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
    @Positive
    private final long employeeId;

    public UpdateBuildingDto(long id, int floors, BigDecimal monthlyTaxPerPerson, BigDecimal monthlyTaxPerPet, BigDecimal monthlyTaxPerM2, long employeeId) {
        this.id = id;
        this.floors = floors;
        this.monthlyTaxPerPerson = monthlyTaxPerPerson;
        this.monthlyTaxPerPet = monthlyTaxPerPet;
        this.monthlyTaxPerM2 = monthlyTaxPerM2;
        this.employeeId = employeeId;
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

    public long getEmployeeId() {
        return employeeId;
    }

    @Override
    public String toString() {
        return "UpdateBuildingDto{" +
                "id=" + id +
                ", floors=" + floors +
                ", monthlyTaxPerPerson=" + monthlyTaxPerPerson +
                ", monthlyTaxPerPet=" + monthlyTaxPerPet +
                ", monthlyTaxPerM2=" + monthlyTaxPerM2 +
                ", employeeId=" + employeeId +
                '}';
    }
}
