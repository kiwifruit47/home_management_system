package org.cscb525.service;

import org.cscb525.dto.monthlyApartmentTax.MonthlyApartmentTaxReceiptDto;
import org.cscb525.service.util.MonthlyTaxReceiptTxtExporter;
import org.cscb525.service.util.MonthlyTaxReceiptXlsAppender;


public class ExportService {
    public static void exportReceipt(MonthlyApartmentTaxReceiptDto receipt) {
        MonthlyTaxReceiptTxtExporter.export(receipt);
        MonthlyTaxReceiptXlsAppender.append(receipt);
    }
}

