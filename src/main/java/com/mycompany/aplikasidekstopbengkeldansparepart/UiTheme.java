package com.mycompany.aplikasidekstopbengkeldansparepart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;

public final class UiTheme {

    public static final Color PRIMARY = new Color(9, 87, 166);
    public static final Color PRIMARY_DARK = new Color(6, 63, 120);
    public static final Color SUCCESS = new Color(30, 132, 73);
    public static final Color WARNING = new Color(175, 116, 0);

    public static final Color BACKGROUND = new Color(243, 246, 250);
    public static final Color SURFACE = Color.WHITE;
    public static final Color BORDER = new Color(216, 222, 230);

    public static final Color TEXT_PRIMARY = new Color(22, 28, 36);
    public static final Color TEXT_MUTED = new Color(94, 109, 124);

    public static final Color SIDEBAR_BG = new Color(237, 243, 250);
    public static final Color SIDEBAR_BUTTON = new Color(226, 236, 247);

    public static final Font FONT_H1 = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_H2 = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    public static final Insets FIELD_INSETS = new Insets(8, 10, 8, 10);

    private UiTheme() {
    }

    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BODY);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY);
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        return button;
    }

    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BODY);
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(SIDEBAR_BUTTON);
        button.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        return button;
    }

    public static JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_BODY);
        field.setMargin(FIELD_INSETS);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        return field;
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(28);
        table.setGridColor(BORDER);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableHeader.setBackground(new Color(233, 240, 248));
        tableHeader.setForeground(TEXT_PRIMARY);
    }
}
