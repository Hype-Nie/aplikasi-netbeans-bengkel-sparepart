package com.mycompany.aplikasidekstopbengkeldansparepart.model;

import java.math.BigDecimal;

public class PurchaseItem {

    private String partCode;
    private String partName;
    private int qty;
    private BigDecimal unitPrice;

    public PurchaseItem() {
    }

    public PurchaseItem(String partCode, String partName, int qty, BigDecimal unitPrice) {
        this.partCode = partCode;
        this.partName = partName;
        this.qty = qty;
        this.unitPrice = unitPrice;
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
