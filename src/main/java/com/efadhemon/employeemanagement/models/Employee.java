package com.efadhemon.employeemanagement.models;

public class Employee {
    private String id;
    private String name;
    private String role;

    public Employee() {
    }

    public Employee(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String toDBLine() {
        return id + "," + name + "," + role;
    }

    public Employee lineToEmployee(String line) {
        String[] fields = line.split(",");
        return new Employee(fields[0], fields[1], fields[2]);
    }
}