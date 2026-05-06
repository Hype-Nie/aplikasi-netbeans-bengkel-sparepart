package com.mycompany.aplikasidekstopbengkeldansparepart.model;

import java.math.BigDecimal;

public class ServiceItem {

    private String itemType;
    private Integer sparepartId;  // FK to spareparts.id, null when item_type = 'JASA'
    private Integer serviceId;    // FK to services.id, null when item_type = 'PART'
    private String description;
    private int qty;
    private BigDecimal price;

    public ServiceItem() {
    }

    public ServiceItem(String itemType, Integer sparepartId, Integer serviceId,
                        String description, int qty, BigDecimal price) {
        this.itemType = itemType;
        this.sparepartId = sparepartId;
        this.serviceId = serviceId;
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

    public Integer getSparepartId() {
        return sparepartId;
    }

    public void setSparepartId(Integer sparepartId) {
        this.sparepartId = sparepartId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
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
