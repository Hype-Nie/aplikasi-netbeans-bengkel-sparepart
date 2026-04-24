package com.mycompany.aplikasidekstopbengkeldansparepart.model;

public class Customer {

    private Integer id;
    private String customerCode;
    private String name;
    private String phone;
    private String vehicle;
    private String address;

    public Customer() {
    }

    public Customer(
            Integer id,
            String customerCode,
            String name,
            String phone,
            String vehicle,
            String address
    ) {
        this.id = id;
        this.customerCode = customerCode;
        this.name = name;
        this.phone = phone;
        this.vehicle = vehicle;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
