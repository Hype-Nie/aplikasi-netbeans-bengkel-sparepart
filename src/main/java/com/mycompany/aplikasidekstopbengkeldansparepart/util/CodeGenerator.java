package com.mycompany.aplikasidekstopbengkeldansparepart.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class CodeGenerator {

    private static final DateTimeFormatter YEAR_MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyyMM");

    private CodeGenerator() {
    }

    public static String currentYearMonth() {
        return LocalDate.now().format(YEAR_MONTH_FORMAT);
    }

    public static String nextCode(String prefix, String yearMonth, String lastCode) {
        int nextNumber = 1;
        if (lastCode != null && !lastCode.isBlank()) {
            int lastDash = lastCode.lastIndexOf('-');
            if (lastDash >= 0 && lastDash + 1 < lastCode.length()) {
                String numberPart = lastCode.substring(lastDash + 1);
                try {
                    nextNumber = Integer.parseInt(numberPart) + 1;
                } catch (NumberFormatException ex) {
                    nextNumber = 1;
                }
            }
        }

        return String.format("%s-%s-%04d", prefix, yearMonth, nextNumber);
    }

    public static String likePattern(String prefix, String yearMonth) {
        return prefix + "-" + yearMonth + "-%";
    }
}
