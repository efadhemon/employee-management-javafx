package com.efadhemon.employeemanagement.controllers;

import com.efadhemon.employeemanagement.App;
import com.efadhemon.employeemanagement.managers.AuthManager;
import com.efadhemon.employeemanagement.models.User;
import com.efadhemon.employeemanagement.utils.Functions;
import com.efadhemon.employeemanagement.utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void handleLogin() {
        System.out.println("Logged in  ");
        String username = usernameField.getText();
        String password = passwordField.getText();

        AuthManager authManager = new AuthManager();

        boolean isAuthenticated = authManager.login(username, password);

        if (isAuthenticated) {
            Session.isAuthenticated = true;
            Session.currentUser = new User(username);
         try {
             Stage stage = (Stage) usernameField.getScene().getWindow();
             FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
             Scene scene = new Scene(fxmlLoader.load(), 586, 586);
             stage.setScene(scene);
         }catch (Exception e){
             e.printStackTrace();
             Functions.showError("Error", e.getMessage());
         }
        } else {
            Functions.showError("Login Filed!", "Wrong username or password");
        }

    }



}
