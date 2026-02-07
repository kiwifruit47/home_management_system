package service;

import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxReceiptDto;
import org.cscb525.service.util.MonthlyTaxReceiptTxtExporter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class MonthlyTaxReceiptTxtExporterTest {
    private final Path path = Path.of("/Users/victoriadimitrova/Documents/Java & DB/home_management_system/receipts/receipt_2026-02_Test address_1.txt");


    @AfterEach
    void cleanup() throws IOException {
        Files.delete(path);
    }

    @Test
    void export_validReceipt_createsFileWithCorrectContent() throws IOException {
        MonthlyApartmentTaxReceiptDto receipt =
                new MonthlyApartmentTaxReceiptDto(
                        YearMonth.of(2026, 2),
                        LocalDate.of(2026, 2, 1),
                        BigDecimal.valueOf(20),
                        1,
                        "Test address",
                        "Test employee",
                        "Test company"
                );

        MonthlyTaxReceiptTxtExporter.export(receipt);

        assertTrue(Files.exists(path));

        String content = Files.readString(path);

        assertTrue(content.contains("PAYMENT FOR MONTH: 2026-02"));
        assertTrue(content.contains("PAYMENT DATE: 2026-02-01"));
        assertTrue(content.contains("PAYMENT VALUE: 20"));
        assertTrue(content.contains("APARTMENT №: 1"));
        assertTrue(content.contains("ADDRESS: Test address"));
        assertTrue(content.contains("EMPLOYEE NAME: Test employee"));
        assertTrue(content.contains("COMPANY NAME: Test company"));
    }
}
