package org.cscb525.service.exporter;

import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxReceiptDto;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MonthlyTaxReceiptTxtExporter {
    public static void export(
            MonthlyApartmentTaxReceiptDto receipt
    ) {
        Path file = Path.of(
                "receipts",
                "receipt_" + receipt.getPaymentForMonth() + "_" +
                        receipt.getApartmentNumber() + ".txt"
        );

        try (BufferedWriter writer = Files.newBufferedWriter(file)) {

            writer.write("MONTH: " + receipt.getPaymentForMonth());
            writer.newLine();
            writer.write("DATE: " + receipt.getDateOfPayment());
            writer.newLine();
            writer.write("VALUE: " + receipt.getPaymentValue());
            writer.newLine();
            writer.write("APARTMENT: " + receipt.getApartmentNumber());
            writer.newLine();
            writer.write("ADDRESS: " + receipt.getAddress());
            writer.newLine();
            writer.write("EMPLOYEE: " + receipt.getEmployeeName());
            writer.newLine();
            writer.write("COMPANY: " + receipt.getCompanyName());

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
