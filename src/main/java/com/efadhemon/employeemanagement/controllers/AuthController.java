package com.efadhemon.employeemanagement.controllers;

import com.efadhemon.employeemanagement.managers.AuthManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AuthController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    protected void handleLogin() {
        System.out.println("Logged in  ");
        String username = usernameField.getText();
        String password = passwordField.getText();

        AuthManager authManager = new AuthManager();

        boolean isAuthenticated = authManager.login(username, password);

        if (isAuthenticated) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Login Successful");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password");
            alert.showAndWait();
        }

    }


}
