package com.mycompany.aplikasidekstopbengkeldansparepart.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ServiceTransaction {

    private String serviceNo;
    private LocalDate serviceDate;
    private String customerCode;
    private String vehicle;
    private String complaint;
    private String mechanic;
    private String status;
    private BigDecimal total;

    public ServiceTransaction() {
    }

    public ServiceTransaction(
            String serviceNo,
            LocalDate serviceDate,
            String customerCode,
            String vehicle,
            String complaint,
            String mechanic,
            String status,
            BigDecimal total
    ) {
        this.serviceNo = serviceNo;
        this.serviceDate = serviceDate;
        this.customerCode = customerCode;
        this.vehicle = vehicle;
        this.complaint = complaint;
        this.mechanic = mechanic;
        this.status = status;
        this.total = total;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getMechanic() {
        return mechanic;
    }

    public void setMechanic(String mechanic) {
        this.mechanic = mechanic;
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
