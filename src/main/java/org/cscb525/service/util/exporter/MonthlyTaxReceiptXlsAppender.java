package org.cscb525.service.util.exporter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxReceiptDto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class MonthlyTaxReceiptXlsAppender {
    private static final Path XLS_PATH =
            Path.of("exports", "payments.xls");

    public static void append(MonthlyApartmentTaxReceiptDto receipt) {
        try {
            Files.createDirectories(XLS_PATH.getParent());

            boolean fileExists = Files.exists(XLS_PATH);

            Workbook workbook;
            Sheet sheet;

            if (fileExists) {
                try (InputStream is = Files.newInputStream(XLS_PATH)) {
                    workbook = new HSSFWorkbook(is);
                }
                sheet = workbook.getSheetAt(0);
            } else {
                workbook = new HSSFWorkbook();
                sheet = workbook.createSheet("Paid Taxes");
                createHeaderRow(sheet);
            }

            int nextRowNum = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(nextRowNum);

            fillRow(row, receipt);

            try (OutputStream os = Files.newOutputStream(
                    XLS_PATH,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            )) {
                workbook.write(os);
            }

            workbook.close();

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to append receipt to XLS", e);
        }
    }

    private static void createHeaderRow(Sheet sheet) {
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Month");
        header.createCell(1).setCellValue("Payment Date");
        header.createCell(2).setCellValue("Value");
        header.createCell(3).setCellValue("Apartment");
        header.createCell(4).setCellValue("Address");
        header.createCell(5).setCellValue("Employee");
        header.createCell(6).setCellValue("Company");
    }

    private static void fillRow(Row row, MonthlyApartmentTaxReceiptDto receipt) {
        row.createCell(0).setCellValue(receipt.getPaymentForMonth().toString());
        row.createCell(1).setCellValue(receipt.getDateOfPayment().toString());
        row.createCell(2).setCellValue(receipt.getPaymentValue().doubleValue());
        row.createCell(3).setCellValue(receipt.getApartmentNumber());
        row.createCell(4).setCellValue(receipt.getAddress());
        row.createCell(5).setCellValue(receipt.getEmployeeName());
        row.createCell(6).setCellValue(receipt.getCompanyName());
    }
}
