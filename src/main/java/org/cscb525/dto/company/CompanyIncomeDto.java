package org.cscb525.dto.company;

import java.math.BigDecimal;

public class CompanyIncomeDto {
    private String companyName;
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
