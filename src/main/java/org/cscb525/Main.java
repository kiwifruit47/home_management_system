package org.cscb525;

import org.cscb525.config.SessionFactoryUtil;

public class Main {
    public static void main(String[] args) {
        SessionFactoryUtil.getSessionFactory().openSession();
//        MonthlyApartmentTaxReceiptDto receipt = new MonthlyApartmentTaxReceiptDto(
//                YearMonth.now(),
//                LocalDate.now(),
//                BigDecimal.valueOf(20),
//                1,
//                "test address",
//                "Test employee",
//                "Test company"
//        );
//        MonthlyTaxReceiptTxtExporter.export(receipt);
//        MonthlyTaxReceiptXlsAppender.append(receipt);
    }
}