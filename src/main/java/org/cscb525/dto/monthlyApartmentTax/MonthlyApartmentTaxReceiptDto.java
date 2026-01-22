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
    private YearMonth paymentForMonth;
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
    @NotBlank(message = "Company name cannot be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Company name must start with a capital letter and consist only of letters")
    private String companyName;
}
