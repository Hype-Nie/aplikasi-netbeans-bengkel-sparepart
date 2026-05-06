package com.mycompany.aplikasidekstopbengkeldansparepart.util;

import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.PurchaseTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.SaleItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.SaleTransaction;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceItem;
import com.mycompany.aplikasidekstopbengkeldansparepart.model.ServiceTransaction;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for printing transaction receipts.
 * Uses Java's built-in java.awt.print API — no external dependencies required.
 * Formats thermal-receipt-style output (~72mm width).
 */
public final class ReceiptPrinter {

    private static final String SHOP_NAME = "BengkelPro";
    private static final String SHOP_SUBTITLE = "Bengkel & Sparepart";
    private static final String SHOP_FOOTER = "Terima kasih atas kepercayaan Anda!";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Receipt width in points (72mm ≈ 204pt)
    private static final double RECEIPT_WIDTH = 204;

    private ReceiptPrinter() {
    }

    // ==================== PUBLIC API ====================

    /**
     * Prints a sale transaction receipt.
     * Shows the system print dialog and returns true if printing was successful.
     */
    public static boolean printSaleReceipt(SaleTransaction tx, List<SaleItem> items, String customerName) {
        List<String> lines = new ArrayList<>();

        addHeader(lines);
        addSeparator(lines);

        lines.add("No. Penjualan : " + tx.getSaleNo());
        lines.add("Tanggal       : " + tx.getSaleDate().format(DATE_FMT));
        String custDisplay = (customerName == null || customerName.isBlank()) ? "Umum (Walk-in)" : customerName;
        lines.add("Pelanggan     : " + custDisplay);
        lines.add("Metode Bayar  : " + (tx.getPaymentMethod() == null ? "Tunai" : tx.getPaymentMethod()));

        addSeparator(lines);
        lines.add("DETAIL ITEM:");
        addDottedSeparator(lines);

        for (SaleItem item : items) {
            lines.add(item.getPartName());
            lines.add(formatItemLine(item.getQty(), item.getUnitPrice(), item.getSubtotal()));
        }

        addDottedSeparator(lines);
        lines.add(rightAlign("TOTAL: " + MoneyUtil.format(tx.getTotal())));
        addSeparator(lines);

        addFooter(lines);

        return doPrint(lines, "Struk Penjualan - " + tx.getSaleNo());
    }

    /**
     * Prints a service transaction receipt.
     * Shows the system print dialog and returns true if printing was successful.
     */
    public static boolean printServiceReceipt(ServiceTransaction tx, List<ServiceItem> items, String customerName) {
        List<String> lines = new ArrayList<>();

        addHeader(lines);
        addSeparator(lines);

        lines.add("No. Servis    : " + tx.getServiceNo());
        lines.add("Tanggal       : " + tx.getServiceDate().format(DATE_FMT));
        String custDisplay = (customerName == null || customerName.isBlank()) ? "-" : customerName;
        lines.add("Pelanggan     : " + custDisplay);
        lines.add("Kendaraan     : " + (tx.getVehicle() == null ? "-" : tx.getVehicle()));
        lines.add("Keluhan       : " + (tx.getComplaint() == null ? "-" : tx.getComplaint()));
        lines.add("Mekanik       : " + (tx.getMechanic() == null ? "-" : tx.getMechanic()));
        lines.add("Status        : " + (tx.getStatus() == null ? "-" : tx.getStatus()));

        addSeparator(lines);
        lines.add("DETAIL ITEM:");
        addDottedSeparator(lines);

        for (ServiceItem item : items) {
            String typeTag = "JASA".equalsIgnoreCase(item.getItemType()) ? "[Jasa] " : "[Part] ";
            lines.add(typeTag + item.getDescription());
            lines.add(formatItemLine(item.getQty(), item.getPrice(), item.getSubtotal()));
        }

        addDottedSeparator(lines);
        lines.add(rightAlign("TOTAL: " + MoneyUtil.format(tx.getTotal())));
        addSeparator(lines);

        addFooter(lines);

        return doPrint(lines, "Struk Servis - " + tx.getServiceNo());
    }

    /**
     * Prints a purchase transaction receipt.
     * Shows the system print dialog and returns true if printing was successful.
     */
    public static boolean printPurchaseReceipt(PurchaseTransaction tx, List<PurchaseItem> items, String supplierName) {
        List<String> lines = new ArrayList<>();

        addHeader(lines);
        addSeparator(lines);

        lines.add("No. Pembelian : " + tx.getPurchaseNo());
        lines.add("Tanggal       : " + tx.getPurchaseDate().format(DATE_FMT));
        String supplierDisplay = (supplierName == null || supplierName.isBlank()) ? "-" : supplierName;
        lines.add("Supplier      : " + supplierDisplay);
        lines.add("Metode Bayar  : " + (tx.getPaymentMethod() == null ? "Transfer" : tx.getPaymentMethod()));
        lines.add("Status        : " + (tx.getStatus() == null ? "-" : tx.getStatus()));

        addSeparator(lines);
        lines.add("DETAIL ITEM:");
        addDottedSeparator(lines);

        for (PurchaseItem item : items) {
            lines.add(item.getPartName());
            lines.add(formatItemLine(item.getQty(), item.getUnitPrice(), item.getSubtotal()));
        }

        addDottedSeparator(lines);
        lines.add(rightAlign("TOTAL: " + MoneyUtil.format(tx.getTotal())));
        addSeparator(lines);

        addFooter(lines);

        return doPrint(lines, "Struk Pembelian - " + tx.getPurchaseNo());
    }

    // ==================== RECEIPT BUILDING HELPERS ====================

    private static void addHeader(List<String> lines) {
        lines.add(centerText(SHOP_NAME));
        lines.add(centerText(SHOP_SUBTITLE));
        lines.add("");
    }

    private static void addFooter(List<String> lines) {
        lines.add("");
        lines.add(centerText(SHOP_FOOTER));
        lines.add(centerText("--- Dicetak otomatis ---"));
    }

    private static void addSeparator(List<String> lines) {
        lines.add("================================");
    }

    private static void addDottedSeparator(List<String> lines) {
        lines.add("--------------------------------");
    }

    private static String formatItemLine(int qty, BigDecimal unitPrice, BigDecimal subtotal) {
        return "  " + qty + " x " + MoneyUtil.format(unitPrice) + " = " + MoneyUtil.format(subtotal);
    }

    private static String centerText(String text) {
        int lineWidth = 32; // character width for thermal receipts
        if (text.length() >= lineWidth) return text;
        int padding = (lineWidth - text.length()) / 2;
        return " ".repeat(padding) + text;
    }

    private static String rightAlign(String text) {
        int lineWidth = 32;
        if (text.length() >= lineWidth) return text;
        int padding = lineWidth - text.length();
        return " ".repeat(padding) + text;
    }

    // ==================== PRINT ENGINE ====================

    /**
     * Sends the formatted receipt lines to the system printer via print dialog.
     */
    private static boolean doPrint(List<String> lines, String jobName) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setJobName(jobName);

        // Configure page format for thermal receipt paper
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = new Paper();
        // Width: 72mm ≈ 204pt, Height: long enough for content
        double height = Math.max(400, lines.size() * 14 + 80);
        paper.setSize(RECEIPT_WIDTH, height);
        paper.setImageableArea(8, 8, RECEIPT_WIDTH - 16, height - 16);
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(PageFormat.PORTRAIT);

        printerJob.setPrintable(new ReceiptPrintable(lines), pageFormat);

        // Show the system print dialog
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
                return true;
            } catch (PrinterException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false; // user cancelled print dialog
    }

    /**
     * Printable implementation that renders receipt lines using monospaced font.
     */
    private static class ReceiptPrintable implements Printable {

        private final List<String> lines;

        ReceiptPrintable(List<String> lines) {
            this.lines = lines;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            // Use monospaced font for receipt alignment
            Font headerFont = new Font(Font.MONOSPACED, Font.BOLD, 10);
            Font bodyFont = new Font(Font.MONOSPACED, Font.PLAIN, 8);

            int y = 12;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                // Use larger bold font for shop name (first line)
                if (i == 0) {
                    g2d.setFont(headerFont);
                } else {
                    g2d.setFont(bodyFont);
                }

                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(line, 0, y);
                y += fm.getHeight() + 2;
            }

            return PAGE_EXISTS;
        }
    }
}
