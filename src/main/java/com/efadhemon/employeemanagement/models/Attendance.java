package com.efadhemon.employeemanagement.models;

public class Attendance {
    public String employeeId;
    public String date;
    public String checkIn;
    public String checkOut;

    public Attendance() {
    }

    public Attendance(String employeeId, String date, String checkIn, String checkOut) {
        this.employeeId = employeeId;
        this.date = date;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Attendance lineToAttendance(String line) {
        String[] parts = line.split(",");
        String employeeId = parts.length > 0 ? parts[0] : "";
        String date = parts.length > 1 ? parts[1] : "";
        String checkIn = parts.length > 2 ? parts[2] : "";
        String checkOut = parts.length > 3 ? parts[3] : "";
        return new Attendance(employeeId, date, checkIn, checkOut);
    }
}
