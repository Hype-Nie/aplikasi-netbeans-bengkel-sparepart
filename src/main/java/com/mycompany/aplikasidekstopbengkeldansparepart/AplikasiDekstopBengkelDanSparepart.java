/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.aplikasidekstopbengkeldansparepart;

import com.mycompany.aplikasidekstopbengkeldansparepart.controller.LoginController;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author umift
 */
public class AplikasiDekstopBengkelDanSparepart {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            installLookAndFeel();
            new LoginController().show();
        });
    }

    private static void installLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Gagal menerapkan look and feel sistem: " + ex.getMessage());
        }
    }
}
