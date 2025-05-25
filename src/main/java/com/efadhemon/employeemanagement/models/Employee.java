package com.efadhemon.employeemanagement.models;

public class Employee {
    public String id;
    public String name;
    public String role;

    public Employee(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}