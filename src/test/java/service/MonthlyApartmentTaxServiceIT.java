package service;

import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxDto;
import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxEmployeeDto;
import org.cscb525.exceptions.EmptyListException;
import org.cscb525.exceptions.NotFoundException;
import org.cscb525.service.MonthlyApartmentTaxService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MonthlyApartmentTaxServiceIT {
    private final MonthlyApartmentTaxService taxService = new MonthlyApartmentTaxService();

    @BeforeEach
    void setup() {
        TestDataSetup.setupData();
    }

    @AfterEach
    void cleanup() {
        TestDataSetup.clearDB();
    }


    @Test
    public void getMonthlyApartmentTax_existingTax_returnsDto() {
        MonthlyApartmentTaxDto tax = taxService.getMonthlyApartmentTaxById(1);

        assertNotNull(tax);
    }

    @Test
    public void getAllMonthlyApartmentTaxes_existingTaxes_returnsList() {
        List<MonthlyApartmentTaxDto> taxes = taxService.getAllMonthlyApartmentTaxes();

        assertNotNull(taxes);
        assertEquals(50, taxes.size());
    }

    @Test
    public void getAllMonthlyApartmentTaxes_noTaxes_throwsException() {
        cleanup();
        assertThrows(
                EmptyListException.class,
                taxService::getAllMonthlyApartmentTaxes
        );
    }

    @Test
    public void markTaxAsPaid_existingTax_marksAsPaid() {
        MonthlyApartmentTaxDto updated = taxService.markTaxAsPaid(1);

        assertTrue(updated.isPaid());
    }

    @Test
    public void taxPaymentStatus_paidTax_returnsTrue() {
        taxService.markTaxAsPaid(1);

        boolean status = taxService.taxPaymentStatus(1);

        assertTrue(status);
    }

    @Test
    public void deleteMonthlyApartmentTax_existingTax_deletesTax() {
        taxService.deleteMonthlyApartmentTax(1);

        assertThrows(
                NotFoundException.class,
                () -> taxService.getMonthlyApartmentTaxById(1)
        );
    }

    @Test
    public void restoreMonthlyApartmentTax_deletedTax_restoresTax() {
        taxService.deleteMonthlyApartmentTax(1);

        taxService.restoreMonthlyApartmentTax(1);

        assertDoesNotThrow(
                () -> taxService.getMonthlyApartmentTaxById(1)
        );
    }

    @Test
    public void getPaidTaxesSumByApartment_returnsNonNegativeValue() {
        MonthlyApartmentTaxDto tax = taxService.markTaxAsPaid(1);

        BigDecimal sum = taxService.getPaidTaxesSumByApartment(1);

        assertNotNull(sum);
        assertEquals(0, sum.compareTo(tax.getPaymentValue()));
    }

    @Test
    public void getPaidTaxesSumByBuilding_returnsValue() {
        MonthlyApartmentTaxDto tax = taxService.markTaxAsPaid(1);

        BigDecimal sum = taxService.getPaidTaxesSumByBuilding(1);

        assertEquals(0, sum.compareTo(tax.getPaymentValue()));
    }

    @Test
    public void getPaidTaxesSumByEmployee_returnsValue() {
        MonthlyApartmentTaxDto tax = taxService.markTaxAsPaid(1);

        BigDecimal sum = taxService.getPaidTaxesSumByEmployee(1);

        assertEquals(0, sum.compareTo(tax.getPaymentValue()));
    }

    @Test
    public void getPaidTaxesSumByCompany_returnsValue() {
        MonthlyApartmentTaxDto tax = taxService.markTaxAsPaid(1);

        BigDecimal sum = taxService.getPaidTaxesSumByBuCompany(1);

        assertEquals(0, sum.compareTo(tax.getPaymentValue()));
    }

    @Test
    public void getAllPaidMonthlyApartmentTaxesByCompany_returnsList() {
        taxService.markTaxAsPaid(1);

        List<MonthlyApartmentTaxEmployeeDto> taxes =
                taxService.getAllPaidMonthlyApartmentTaxesByCompany(1);

        assertFalse(taxes.isEmpty());
    }

    @Test
    public void getAllUnpaidMonthlyApartmentTaxesByCompany_returnsList() {
        taxService.getMonthlyApartmentTaxById(1);

        List<MonthlyApartmentTaxEmployeeDto> taxes =
                taxService.getAllUnpaidMonthlyApartmentTaxesByCompany(1);

        assertFalse(taxes.isEmpty());
    }

    @Test
    public void calculateMonthlyTaxValue_validApartment_returnsPositiveValue() {
        BigDecimal value = MonthlyApartmentTaxService.calculateMonthlyTaxValue(1);

        assertNotNull(value);
        assertTrue(value.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void calculateMonthlyTaxValue_invalidApartment_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> MonthlyApartmentTaxService.calculateMonthlyTaxValue(0)
        );
    }
}
