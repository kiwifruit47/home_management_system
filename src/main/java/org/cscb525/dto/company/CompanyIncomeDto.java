package org.cscb525.dto.company;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CompanyIncomeDto {
    @NotBlank(message = "Company name cannot be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Company name must start with a capital letter and consist only of letters")
    private String companyName;
    @PositiveOrZero
    private BigDecimal income;

    public CompanyIncomeDto(String companyName, BigDecimal income) {
        this.companyName = companyName;
        this.income = income;
    }

    public String getCompanyName() {
        return companyName;
    }

    public BigDecimal getIncome() {
        return income;
    }

    @Override
    public String toString() {
        return "CompanyIncomeDto{" +
                "companyName='" + companyName + '\'' +
                ", income=" + income +
                '}';
    }
}

