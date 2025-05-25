package com.efadhemon.employeemanagement.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmployeeManagementController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}