package com.mycompany.aplikasidekstopbengkeldansparepart.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PurchaseTransaction {

    private String purchaseNo;
    private LocalDate purchaseDate;
    private String supplierCode;
    private String paymentMethod;
    private String status;
    private BigDecimal total;

    public PurchaseTransaction() {
    }

    public PurchaseTransaction(
            String purchaseNo,
            LocalDate purchaseDate,
            String supplierCode,
            String paymentMethod,
            String status,
            BigDecimal total
    ) {
        this.purchaseNo = purchaseNo;
        this.purchaseDate = purchaseDate;
        this.supplierCode = supplierCode;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.total = total;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
