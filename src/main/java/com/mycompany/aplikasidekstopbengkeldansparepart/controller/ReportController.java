package com.mycompany.aplikasidekstopbengkeldansparepart.controller;

import com.mycompany.aplikasidekstopbengkeldansparepart.dao.ReportDao;
import com.mycompany.aplikasidekstopbengkeldansparepart.view.panel.ReportPanelView;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class ReportController {

    private final ReportPanelView view;
    private final ReportDao dao;

    public ReportController(ReportPanelView view, ReportDao dao) {
        this.view = view;
        this.dao = dao;
        bindActions();
        handleLoadReport();
    }

    private void bindActions() {
        view.addLoadReportListener(e -> handleLoadReport());
        view.addExportServiceCsvListener(e -> handleExportCsv(
                view.getServiceModel(), "laporan_servis"));
        view.addExportPurchaseCsvListener(e -> handleExportCsv(
                view.getPurchaseModel(), "laporan_pembelian"));
        view.addExportSaleCsvListener(e -> handleExportCsv(
                view.getSaleModel(), "laporan_penjualan"));
    }

    private void handleLoadReport() {
        String dateFrom = view.getDateFrom();
        String dateTo = view.getDateTo();

        if (dateFrom.isBlank() || dateTo.isBlank()) {
            JOptionPane.showMessageDialog(view,
                    "Tanggal dari dan tanggal sampai wajib diisi.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Object[]> serviceRows = dao.findServiceTransactions(dateFrom, dateTo);
            List<Object[]> purchaseRows = dao.findPurchaseTransactions(dateFrom, dateTo);
            List<Object[]> saleRows = dao.findSaleTransactions(dateFrom, dateTo);
            BigDecimal totalService = dao.sumServiceTotal(dateFrom, dateTo);
            BigDecimal totalPurchase = dao.sumPurchaseTotal(dateFrom, dateTo);
            BigDecimal totalSale = dao.sumSaleTotal(dateFrom, dateTo);

            view.setServiceRows(serviceRows);
            view.setPurchaseRows(purchaseRows);
            view.setSaleRows(saleRows);
            view.setSummary(totalService, totalPurchase, totalSale);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view,
                    "Gagal memuat data laporan: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleExportCsv(DefaultTableModel model, String defaultName) {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(view,
                    "Tidak ada data untuk diekspor. Tampilkan laporan terlebih dahulu.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export CSV");
        chooser.setSelectedFile(new java.io.File(defaultName + ".csv"));
        chooser.setFileFilter(new FileNameExtensionFilter("CSV files (*.csv)", "csv"));

        if (chooser.showSaveDialog(view) != JFileChooser.APPROVE_OPTION) return;

        java.io.File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv"))
            file = new java.io.File(file.getAbsolutePath() + ".csv");

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            StringBuilder header = new StringBuilder();
            for (int col = 0; col < model.getColumnCount(); col++) {
                if (col > 0) header.append(",");
                header.append(escapeCsv(model.getColumnName(col)));
            }
            writer.println(header);

            for (int row = 0; row < model.getRowCount(); row++) {
                StringBuilder line = new StringBuilder();
                for (int col = 0; col < model.getColumnCount(); col++) {
                    if (col > 0) line.append(",");
                    Object val = model.getValueAt(row, col);
                    line.append(escapeCsv(val == null ? "" : val.toString()));
                }
                writer.println(line);
            }

            JOptionPane.showMessageDialog(view,
                    "Data berhasil diekspor ke:\n" + file.getAbsolutePath(),
                    "Export Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view,
                    "Gagal menulis file CSV: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
