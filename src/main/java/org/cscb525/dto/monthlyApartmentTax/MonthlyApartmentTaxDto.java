package org.cscb525.dto.monthlyApartmentTax;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.YearMonth;

public class MonthlyApartmentTaxDto {
    @NotNull
    private YearMonth paymentForMonth;
    private boolean isPaid;
    @NotNull
    @PositiveOrZero
    private BigDecimal paymentValue;
    @Positive
    private int apartmentNumber;
    @NotBlank(message = "Address cannot be blank")
    private String address;

    public MonthlyApartmentTaxDto(YearMonth paymentForMonth, boolean isPaid, BigDecimal paymentValue, int apartmentNumber, String address) {
        this.paymentForMonth = paymentForMonth;
        this.isPaid = isPaid;
        this.paymentValue = paymentValue;
        this.apartmentNumber = apartmentNumber;
        this.address = address;
    }

    public YearMonth getPaymentForMonth() {
        return paymentForMonth;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public BigDecimal getPaymentValue() {
        return paymentValue;
    }

    @NotNull
    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public @NotBlank(message = "Address cannot be blank") String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "MonthlyApartmentTaxDto{" +
                "paymentForMonth=" + paymentForMonth +
                ", isPaid=" + isPaid +
                ", paymentValue=" + paymentValue +
                ", apartmentNumber=" + apartmentNumber +
                ", address+" + address +
                '}';
    }
}
