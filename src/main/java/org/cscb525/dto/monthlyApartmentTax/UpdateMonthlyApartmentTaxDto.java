package org.cscb525.dto.monthlyApartmentTax;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UpdateMonthlyApartmentTaxDto {
    private final long id;
    @NotNull
    private final boolean isPaid;
    @NotNull
    private final LocalDate dateOfPayment;

    public UpdateMonthlyApartmentTaxDto(long id, boolean isPaid, LocalDate dateOfPayment) {
        this.id = id;
        this.isPaid = isPaid;
        this.dateOfPayment = dateOfPayment;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public LocalDate getDateOfPayment() {
        return dateOfPayment;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UpdateMonthlyApartmentTaxDto{" +
                "id=" + id +
                ", isPaid=" + isPaid +
                ", dateOfPayment=" + dateOfPayment +
                '}';
    }
}
