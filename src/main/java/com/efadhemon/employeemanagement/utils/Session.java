package com.efadhemon.employeemanagement.utils;


import com.efadhemon.employeemanagement.models.User;

public class Session {
    public static User currentUser = null;
    public static   boolean isAuthenticated =  false;

//    public Session(boolean isAuthenticated, User currentUser) {
//        this.isAuthenticated = isAuthenticated;
//        this.currentUser = currentUser;
//    }
}