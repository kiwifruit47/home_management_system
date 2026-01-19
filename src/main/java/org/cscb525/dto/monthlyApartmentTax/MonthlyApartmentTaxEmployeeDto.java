package org.cscb525.dto.monthlyApartmentTax;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class MonthlyApartmentTaxEmployeeDto {
    @NotNull
    private YearMonth paymentForMonth;
    private boolean isPaid;
    @NotNull
    private LocalDate dateOfPayment;
    @NotNull
    @PositiveOrZero
    private BigDecimal paymentValue;
    @NotNull
    private int apartmentNumber;
    @NotBlank(message = "Address cannot be blank")
    private String address;
    @NotBlank(message = "Employee name cannot be blank")
    private String employeeName;

    public MonthlyApartmentTaxEmployeeDto(YearMonth paymentForMonth, boolean isPaid, LocalDate dateOfPayment, BigDecimal paymentValue, int apartmentNumber, String address, String employeeName) {
        this.paymentForMonth = paymentForMonth;
        this.isPaid = isPaid;
        this.dateOfPayment = dateOfPayment;
        this.paymentValue = paymentValue;
        this.apartmentNumber = apartmentNumber;
        this.address = address;
        this.employeeName = employeeName;
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
    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public @NotBlank(message = "Address cannot be blank") String getAddress() {
        return address;
    }

    public @NotBlank(message = "Employee name cannot be blank") String getEmployeeName() {
        return employeeName;
    }

    @Override
    public String toString() {
        return "MonthlyApartmentTaxEmployeeDto{" +
                "paymentForMonth=" + paymentForMonth +
                ", isPaid=" + isPaid +
                ", dateOfPayment=" + dateOfPayment +
                ", paymentValue=" + paymentValue +
                ", apartmentNumber=" + apartmentNumber +
                ", address='" + address + '\'' +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}
