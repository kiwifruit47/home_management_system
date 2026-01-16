package org.cscb525.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "monthly_apartment_tax")
public class MonthlyApartmentTax extends BaseEntity{
    @NotNull
    @Positive
    @Column(name = "payment_for_month")
    private YearMonth paymentForMonth;
    @Column(name = "is_paid")
    private boolean isPaid = false;
    @Column(name = "date_of_payment")
    @NotNull
    private LocalDate dateOfPayment;
    @Column(name = "payment_value")
    @NotNull
    @Positive
    private BigDecimal paymentValue;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

    public MonthlyApartmentTax() {
    }

    public MonthlyApartmentTax(YearMonth paymentForMonth, BigDecimal paymentValue, Apartment apartment) {
        this.paymentForMonth = paymentForMonth;
        this.paymentValue = paymentValue;
        this.apartment = apartment;
    }

    public YearMonth getPaymentForMonth() {
        return paymentForMonth;
    }

    public void setPaymentForMonth(YearMonth paymentForMonth) {
        this.paymentForMonth = paymentForMonth;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public LocalDate getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(LocalDate dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public BigDecimal getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(BigDecimal paymentValue) {
        this.paymentValue = paymentValue;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @Override
    public String toString() {
        return "MonthlyApartmentTax{" +
                "paymentForMonth=" + paymentForMonth +
                ", isPaid=" + isPaid +
                ", dateOfPayment=" + dateOfPayment +
                ", paymentValue=" + paymentValue +
                ", apartment=" + apartment +
                "} " + super.toString();
    }
}
