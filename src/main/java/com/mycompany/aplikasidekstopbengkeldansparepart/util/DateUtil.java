package com.mycompany.aplikasidekstopbengkeldansparepart.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateUtil() {
    }

    public static String todayText() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    public static LocalDate parse(String value) {
        try {
            return LocalDate.parse(value, DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Format tanggal harus yyyy-MM-dd.");
        }
    }
}
