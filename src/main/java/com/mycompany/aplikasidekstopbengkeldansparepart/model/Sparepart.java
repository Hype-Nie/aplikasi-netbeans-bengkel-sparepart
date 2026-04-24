package com.mycompany.aplikasidekstopbengkeldansparepart.model;

import java.math.BigDecimal;

public class Sparepart {

    private Integer id;
    private String partCode;
    private String name;
    private String category;
    private String unit;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private int stock;
    private int minStock;

    public Sparepart() {
    }

    public Sparepart(
            Integer id,
            String partCode,
            String name,
            String category,
            String unit,
            BigDecimal purchasePrice,
            BigDecimal sellingPrice,
            int stock,
            int minStock
    ) {
        this.id = id;
        this.partCode = partCode;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.stock = stock;
        this.minStock = minStock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }
}
