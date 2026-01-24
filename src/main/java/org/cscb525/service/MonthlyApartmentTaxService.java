package org.cscb525.service;

import org.cscb525.dao.ApartmentDao;
import org.cscb525.dao.MonthlyApartmentTaxDao;
import org.cscb525.dto.monthlyApartmentTax.CalculateMonthlyApartmentTaxDto;
import org.cscb525.dto.monthlyApartmentTax.CreateMonthlyApartmentTaxDto;
import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxReceiptDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class MonthlyApartmentTaxService {
    public static void markTaxAsPaid(long taxId) {
        MonthlyApartmentTaxDao.markTaxAsPaid(taxId, LocalDate.now());

        MonthlyApartmentTaxReceiptDto receipt =
                MonthlyApartmentTaxDao.findPaidMonthlyApartmentTaxById(taxId);

        ExportService.exportReceipt(receipt);
    }


    public static void generateMonthlyTaxesForCurrentMonth() {
        List<Long> allApartmentIds = ApartmentDao.findAllApartmentIds();

        for (Long apartmentId : allApartmentIds) {
            if (MonthlyApartmentTaxDao.taxExistenceCheck(apartmentId, YearMonth.now())) {
                continue;
            }
            BigDecimal paymentValue = calculateMonthlyTaxValue(apartmentId);
            CreateMonthlyApartmentTaxDto taxDto = new CreateMonthlyApartmentTaxDto(YearMonth.now(), paymentValue, apartmentId);
            MonthlyApartmentTaxDao.createMonthlyApartmentTax(taxDto);
        }

    }

    public static BigDecimal calculateMonthlyTaxValue(long apartmentId) {
        CalculateMonthlyApartmentTaxDto calculateTaxDto = MonthlyApartmentTaxDao.findDataForTaxCalc(apartmentId);

        BigDecimal area = calculateTaxDto.getArea();
        BigDecimal occupantCount = BigDecimal.valueOf(calculateTaxDto.getTaxedOccupantCount());
        BigDecimal petCount = BigDecimal.valueOf(calculateTaxDto.getPets());
        BigDecimal taxPerM2 = calculateTaxDto.getMonthlyTaxPerM2();
        BigDecimal taxPerPerson = calculateTaxDto.getMonthlyTaxPerPerson();
        BigDecimal taxPerPet = calculateTaxDto.getMonthlyTaxPerPet();

        return area.multiply(taxPerM2)
                .add(occupantCount.multiply(taxPerPerson))
                .add(petCount.multiply(taxPerPet))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
