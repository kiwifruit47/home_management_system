package org.cscb525.dto.monthlyApartmentTax;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.YearMonth;

public class MonthlyApartmentTaxEmployeeDto {
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
    @NotBlank(message = "Employee name cannot be blank")
    private String employeeName;

    public MonthlyApartmentTaxEmployeeDto(YearMonth paymentForMonth, boolean isPaid, BigDecimal paymentValue, int apartmentNumber, String address, String employeeName) {
        this.paymentForMonth = paymentForMonth;
        this.isPaid = isPaid;
        this.paymentValue = paymentValue;
        this.apartmentNumber = apartmentNumber;
        this.address = address;
        this.employeeName = employeeName;
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

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    @Override
    public String toString() {
        return "MonthlyApartmentTaxEmployeeDto{" +
                "paymentForMonth=" + paymentForMonth +
                ", isPaid=" + isPaid +
                ", paymentValue=" + paymentValue +
                ", apartmentNumber=" + apartmentNumber +
                ", address='" + address + '\'' +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}
