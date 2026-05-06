package com.mycompany.aplikasidekstopbengkeldansparepart.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SaleTransaction {

    private String saleNo;
    private LocalDate saleDate;
    private String customerCode; // nullable for walk-in customers
    private String paymentMethod;
    private BigDecimal total;

    public SaleTransaction() {
    }

    public SaleTransaction(
            String saleNo,
            LocalDate saleDate,
            String customerCode,
            String paymentMethod,
            BigDecimal total
    ) {
        this.saleNo = saleNo;
        this.saleDate = saleDate;
        this.customerCode = customerCode;
        this.paymentMethod = paymentMethod;
        this.total = total;
    }

    public String getSaleNo() {
        return saleNo;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
