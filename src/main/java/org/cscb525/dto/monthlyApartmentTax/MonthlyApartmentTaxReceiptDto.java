package org.cscb525.dto.monthlyApartmentTax;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class MonthlyApartmentTaxReceiptDto {
    @NotNull
    private final YearMonth paymentForMonth;
    @NotNull
    private final LocalDate dateOfPayment;
    @NotNull
    @PositiveOrZero
    private final BigDecimal paymentValue;
    @NotNull
    private final int apartmentNumber;
    @NotBlank(message = "Address cannot be blank")
    private final String address;
    @NotBlank(message = "Employee name cannot be blank")
    private final String employeeName;
    @NotBlank(message = "Company name cannot be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Company name must start with a capital letter and consist only of letters")
    private final String companyName;

    public MonthlyApartmentTaxReceiptDto(YearMonth paymentForMonth, LocalDate dateOfPayment, BigDecimal paymentValue, int apartmentNumber, String address, String employeeName, String companyName) {
        this.paymentForMonth = paymentForMonth;
        this.dateOfPayment = dateOfPayment;
        this.paymentValue = paymentValue;
        this.apartmentNumber = apartmentNumber;
        this.address = address;
        this.employeeName = employeeName;
        this.companyName = companyName;
    }

    public YearMonth getPaymentForMonth() {
        return paymentForMonth;
    }

    public LocalDate getDateOfPayment() {
        return dateOfPayment;
    }

    public BigDecimal getPaymentValue() {
        return paymentValue;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public  String getAddress() {
        return address;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getCompanyName() {
        return companyName;
    }
}
