package org.cscb525.dto.monthlyApartmentTax;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class CreateMonthlyApartmentTaxDto {
    @NotNull
    private final YearMonth paymentForMonth;
    private final boolean isPaid;
    private final LocalDate dateOfPayment;
    @NotNull
    @PositiveOrZero
    private final BigDecimal paymentValue;
    @NotNull
    private final long apartmentId;

    public CreateMonthlyApartmentTaxDto(YearMonth paymentForMonth, boolean isPaid, LocalDate dateOfPayment, BigDecimal paymentValue, long apartmentId) {
        this.paymentForMonth = paymentForMonth;
        this.isPaid = isPaid;
        this.dateOfPayment = dateOfPayment;
        this.paymentValue = paymentValue;
        this.apartmentId = apartmentId;
    }

    public @NotNull YearMonth getPaymentForMonth() {
        return paymentForMonth;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public @NotNull LocalDate getDateOfPayment() {
        return dateOfPayment;
    }

    public @NotNull @PositiveOrZero BigDecimal getPaymentValue() {
        return paymentValue;
    }

    @NotNull
    public long getApartmentId() {
        return apartmentId;
    }

    @Override
    public String toString() {
        return "CreateMonthlyApartmentTaxDto{" +
                "paymentForMonth=" + paymentForMonth +
                ", isPaid=" + isPaid +
                ", dateOfPayment=" + dateOfPayment +
                ", paymentValue=" + paymentValue +
                ", apartmentId=" + apartmentId +
                '}';
    }
}
