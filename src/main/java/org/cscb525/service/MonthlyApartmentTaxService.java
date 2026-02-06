package org.cscb525.service;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.ApartmentDao;
import org.cscb525.dao.MonthlyApartmentTaxDao;
import org.cscb525.dto.monthlyApartmentTax.CalculateMonthlyApartmentTaxDto;
import org.cscb525.dto.monthlyApartmentTax.CreateMonthlyApartmentTaxDto;
import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxDto;
import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxEmployeeDto;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.MonthlyApartmentTax;
import org.cscb525.exceptions.EmptyListException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class MonthlyApartmentTaxService {
    public MonthlyApartmentTaxDto getMonthlyApartmentTaxById(long taxId) {
        return MonthlyApartmentTaxDao.findMonthlyApartmentTaxDtoById(taxId);
    }

    public List<MonthlyApartmentTaxDto> getAllMonthlyApartmentTaxes() {
        List<MonthlyApartmentTaxDto> taxes =
                MonthlyApartmentTaxDao.findAllMonthlyApartmentTaxes();
        if (taxes.isEmpty()) throw new EmptyListException(MonthlyApartmentTax.class);
        return taxes;
    }

    public List<MonthlyApartmentTaxEmployeeDto> getAllPaidMonthlyApartmentTaxesByCompany(long companyId) {
        List<MonthlyApartmentTaxEmployeeDto> taxes =
                MonthlyApartmentTaxDao.findMonthlyApartmentTaxesByCompany(companyId, true);
        if (taxes.isEmpty()) throw new EmptyListException(MonthlyApartmentTax.class);
        return taxes;
    }

    public List<MonthlyApartmentTaxEmployeeDto> getAllUnpaidMonthlyApartmentTaxesByCompany(long companyId) {
        List<MonthlyApartmentTaxEmployeeDto> taxes =
                MonthlyApartmentTaxDao.findMonthlyApartmentTaxesByCompany(companyId, false);
        if (taxes.isEmpty()) throw new EmptyListException(MonthlyApartmentTax.class);
        return taxes;
    }

    public List<MonthlyApartmentTaxDto> getAllPaidMonthlyApartmentTaxesByEmployee(long employeeId) {
        List<MonthlyApartmentTaxDto> taxes =
                MonthlyApartmentTaxDao.findMonthlyApartmentTaxesByEmployee(employeeId, true);
        if (taxes.isEmpty()) throw new EmptyListException(MonthlyApartmentTax.class);
        return taxes;
    }

    public List<MonthlyApartmentTaxDto> getAllUnpaidMonthlyApartmentTaxesByEmployee(long employeeId) {
        List<MonthlyApartmentTaxDto> taxes =
                MonthlyApartmentTaxDao.findMonthlyApartmentTaxesByEmployee(employeeId, false);
        if (taxes.isEmpty()) throw new EmptyListException(MonthlyApartmentTax.class);
        return taxes;
    }

    public List<MonthlyApartmentTaxDto> getAllPaidMonthlyApartmentTaxesByBuilding(long companyId) {
        List<MonthlyApartmentTaxDto> taxes =
                MonthlyApartmentTaxDao.findMonthlyApartmentTaxesByBuilding(companyId, true);
        if (taxes.isEmpty()) throw new EmptyListException(MonthlyApartmentTax.class);
        return taxes;
    }

    public List<MonthlyApartmentTaxDto> getAllUnpaidMonthlyApartmentTaxesByBuilding(long buildingId) {
        List<MonthlyApartmentTaxDto> taxes =
                MonthlyApartmentTaxDao.findMonthlyApartmentTaxesByBuilding(buildingId, false);
        if (taxes.isEmpty()) throw new EmptyListException(MonthlyApartmentTax.class);
        return taxes;
    }

    public BigDecimal getPaidTaxesSumByApartment(long apartmentId) {
        return MonthlyApartmentTaxDao.sumMonthlyApartmentTaxesByApartment(apartmentId, true);
    }

    public BigDecimal getUnpaidTaxesSumByApartment(long apartmentId) {
        return MonthlyApartmentTaxDao.sumMonthlyApartmentTaxesByApartment(apartmentId, false);
    }

    public BigDecimal getPaidTaxesSumByBuilding(long buildingId) {
        return MonthlyApartmentTaxDao.sumMonthlyApartmentTaxesByBuilding(buildingId, true);
    }

    public BigDecimal getUnpaidTaxesSumByBuilding(long buildingId) {
        return MonthlyApartmentTaxDao.sumMonthlyApartmentTaxesByBuilding(buildingId, false);
    }

    public BigDecimal getPaidTaxesSumByEmployee(long employeeId) {
        return MonthlyApartmentTaxDao.sumMonthlyApartmentTaxesByEmployee(employeeId, true);
    }

    public BigDecimal getUnpaidTaxesSumByEmployee(long employeeId) {
        return MonthlyApartmentTaxDao.sumMonthlyApartmentTaxesByEmployee(employeeId, false);
    }

    public BigDecimal getPaidTaxesSumByBuCompany(long companyId) {
        return MonthlyApartmentTaxDao.sumMonthlyApartmentTaxesByCompany(companyId, true);
    }

    public BigDecimal getUnpaidTaxesSumByCompany(long companyId) {
        return MonthlyApartmentTaxDao.sumMonthlyApartmentTaxesByCompany(companyId, false);
    }

    public boolean taxPaymentStatus(long taxId) {
        return MonthlyApartmentTaxDao.findMonthlyApartmentTaxPaymentStatus(taxId);
    }

    public MonthlyApartmentTaxDto deleteMonthlyApartmentTax(long taxId) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();

            MonthlyApartmentTaxDto deletedMonthlyTax = MonthlyApartmentTaxDao.findMonthlyApartmentTaxDtoById(session, taxId);
            MonthlyApartmentTaxDao.deleteMonthlyApartmentTax(session, taxId);

            transaction.commit();

            return deletedMonthlyTax;
        } catch (RuntimeException e) {
            if(transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public MonthlyApartmentTaxDto restoreMonthlyApartmentTax(long taxId) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();

            MonthlyApartmentTaxDto restoredMonthlyTax = MonthlyApartmentTaxDao.findMonthlyApartmentTaxDtoById(session, taxId);
            MonthlyApartmentTaxDao.restoreMonthlyApartmentTax(session, taxId);

            transaction.commit();

            return restoredMonthlyTax;
        } catch (RuntimeException e) {
            if(transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public MonthlyApartmentTaxDto markTaxAsPaid(long taxId) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            MonthlyApartmentTaxDao.markTaxAsPaid(session, taxId, LocalDate.now());
            MonthlyApartmentTaxDto taxDto = MonthlyApartmentTaxDao.findMonthlyApartmentTaxDtoById(session, taxId);

            transaction.commit();
            return taxDto;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }


    public static void generateMonthlyTaxesForCurrentMonth() {
        List<Long> allApartmentIds = ApartmentDao.findAllApartmentIds();

        if (allApartmentIds.isEmpty()) throw new EmptyListException(Apartment.class);

        for (Long apartmentId : allApartmentIds) {
            if (MonthlyApartmentTaxDao.taxExistenceCheck(apartmentId, YearMonth.now())) {
                continue;
            }
            BigDecimal paymentValue = calculateMonthlyTaxValue(apartmentId);
            CreateMonthlyApartmentTaxDto taxDto = new CreateMonthlyApartmentTaxDto(YearMonth.now(), paymentValue, apartmentId);
            if (taxDto.getPaymentValue() == null || paymentValue.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Payment value must be positive");
            }

            MonthlyApartmentTaxDao.createMonthlyApartmentTax(taxDto);
        }

    }

    public static BigDecimal calculateMonthlyTaxValue(long apartmentId) {
        if (apartmentId <= 0) {
            throw new IllegalArgumentException("Apartment id must be positive");
        }

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
