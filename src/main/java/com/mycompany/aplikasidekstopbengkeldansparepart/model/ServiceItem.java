package com.mycompany.aplikasidekstopbengkeldansparepart.model;

import java.math.BigDecimal;

public class ServiceItem {

    private String itemType;
    private String itemCode;
    private String description;
    private int qty;
    private BigDecimal price;

    public ServiceItem() {
    }

    public ServiceItem(String itemType, String itemCode, String description, int qty, BigDecimal price) {
        this.itemType = itemType;
        this.itemCode = itemCode;
        this.description = description;
        this.qty = qty;
        this.price = price;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(qty));
    }
}
