package org.cscb525.dto.monthlyApartmentTax;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UpdateMonthlyApartmentTaxDto {
    @NotNull
    private long id;
    @NotNull
    private boolean isPaid;
    @NotNull
    private LocalDate dateOfPayment;

    public UpdateMonthlyApartmentTaxDto(long id, boolean isPaid, LocalDate dateOfPayment) {
        this.id = id;
        this.isPaid = isPaid;
        this.dateOfPayment = dateOfPayment;
    }

    @NotNull
    public boolean isPaid() {
        return isPaid;
    }

    public @NotNull LocalDate getDateOfPayment() {
        return dateOfPayment;
    }

    @NotNull
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
