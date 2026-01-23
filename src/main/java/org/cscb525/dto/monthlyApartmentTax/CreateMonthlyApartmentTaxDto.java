package org.cscb525.dto.monthlyApartmentTax;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class CreateMonthlyApartmentTaxDto {
    @NotNull
    private final YearMonth paymentForMonth;
    @NotNull
    @PositiveOrZero
    private final BigDecimal paymentValue;
    @NotNull
    private final long apartmentId;

    public CreateMonthlyApartmentTaxDto(YearMonth paymentForMonth, BigDecimal paymentValue, long apartmentId) {
        this.paymentForMonth = paymentForMonth;
        this.paymentValue = paymentValue;
        this.apartmentId = apartmentId;
    }

    public @NotNull YearMonth getPaymentForMonth() {
        return paymentForMonth;
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
                ", paymentValue=" + paymentValue +
                ", apartmentId=" + apartmentId +
                '}';
    }
}
