package com.mycompany.aplikasidekstopbengkeldansparepart.model;

public class Admin {

    private final int id;
    private final String username;
    private final String fullName;
    private final String role;

    public Admin(int id, String username, String fullName, String role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }
}
