package com.mycompany.aplikasidekstopbengkeldansparepart.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class MoneyUtil {

    private static final Locale LOCALE_ID = new Locale("id", "ID");

    private MoneyUtil() {
    }

    public static String format(BigDecimal value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(LOCALE_ID);
        return formatter.format(value == null ? BigDecimal.ZERO : value);
    }

    public static BigDecimal parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return BigDecimal.ZERO;
        }

        String cleaned = raw
                .replace("Rp", "")
                .replace("rp", "")
                .replace(" ", "")
                .replace(".", "")
                .replace(",", ".")
                .replaceAll("[^0-9.-]", "");

        if (cleaned.isBlank() || "-".equals(cleaned)) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(cleaned);
    }
}
