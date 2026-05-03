package com.mycompany.aplikasidekstopbengkeldansparepart.model;

import java.math.BigDecimal;

public class SaleItem {

    private int sparepartId;
    private String partCode;
    private String partName;
    private int qty;
    private BigDecimal unitPrice;

    public SaleItem() {
    }

    public SaleItem(int sparepartId, String partCode, String partName, int qty, BigDecimal unitPrice) {
        this.sparepartId = sparepartId;
        this.partCode = partCode;
        this.partName = partName;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public int getSparepartId() {
        return sparepartId;
    }

    public void setSparepartId(int sparepartId) {
        this.sparepartId = sparepartId;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(qty));
    }
}
