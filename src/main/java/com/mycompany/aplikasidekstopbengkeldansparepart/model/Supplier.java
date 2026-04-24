package com.mycompany.aplikasidekstopbengkeldansparepart.model;

public class Supplier {

    private Integer id;
    private String supplierCode;
    private String name;
    private String phone;
    private String email;
    private String address;

    public Supplier() {
    }

    public Supplier(
            Integer id,
            String supplierCode,
            String name,
            String phone,
            String email,
            String address
    ) {
        this.id = id;
        this.supplierCode = supplierCode;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
