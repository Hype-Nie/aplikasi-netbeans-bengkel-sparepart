package com.mycompany.aplikasidekstopbengkeldansparepart.model;

import java.math.BigDecimal;

public class DashboardSummary {

    private final int activeCustomers;
    private final int totalStock;
    private final int todayServices;
    private final int monthlyPurchases;
    private final BigDecimal todayRevenue;

    public DashboardSummary(
            int activeCustomers,
            int totalStock,
            int todayServices,
            int monthlyPurchases,
            BigDecimal todayRevenue
    ) {
        this.activeCustomers = activeCustomers;
        this.totalStock = totalStock;
        this.todayServices = todayServices;
        this.monthlyPurchases = monthlyPurchases;
        this.todayRevenue = todayRevenue;
    }

    public int getActiveCustomers() {
        return activeCustomers;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public int getTodayServices() {
        return todayServices;
    }

    public int getMonthlyPurchases() {
        return monthlyPurchases;
    }

    public BigDecimal getTodayRevenue() {
        return todayRevenue;
    }
}
