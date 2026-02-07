package org.cscb525.service.util;

import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxReceiptDto;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MonthlyTaxReceiptTxtExporter {
    public static void export(MonthlyApartmentTaxReceiptDto receipt) {
        Path directory = Path.of("receipts");

        Path file = directory.resolve(
                "receipt_" + receipt.getPaymentForMonth() + "_" +
                        receipt.getApartmentNumber() + ".txt"
        );

        try {

            Files.createDirectories(directory);

            try (BufferedWriter writer = Files.newBufferedWriter(file)) {
                writer.write("PAYMENT FOR MONTH: " + receipt.getPaymentForMonth());
                writer.newLine();
                writer.write("PAYMENT DATE: " + receipt.getDateOfPayment());
                writer.newLine();
                writer.write("PAYMENT VALUE: " + receipt.getPaymentValue());
                writer.newLine();
                writer.write("APARTMENT №: " + receipt.getApartmentNumber());
                writer.newLine();
                writer.write("ADDRESS: " + receipt.getAddress());
                writer.newLine();
                writer.write("EMPLOYEE NAME: " + receipt.getEmployeeName());
                writer.newLine();
                writer.write("COMPANY NAME: " + receipt.getCompanyName());
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
