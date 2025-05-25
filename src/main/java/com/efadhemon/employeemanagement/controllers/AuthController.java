package com.efadhemon.employeemanagement.controllers;

import com.efadhemon.employeemanagement.App;
import com.efadhemon.employeemanagement.models.User;
import com.efadhemon.employeemanagement.utils.Functions;
import com.efadhemon.employeemanagement.utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AuthController {
    private static final  String DB_PATH = "src/databases/users.txt";

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void handleLogin() {
        System.out.println("Logged in  ");
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean isAuthenticated = this.login(username, password);

        if (isAuthenticated) {
            Session.isAuthenticated = true;
            Session.currentUser = new User(username);
         try {
             Stage stage = (Stage) usernameField.getScene().getWindow();
             FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
             Scene scene = new Scene(fxmlLoader.load(), 586, 586);
             stage.setScene(scene);
         }catch (Exception e){
             Functions.showError("Error", e.getMessage());
         }
        } else {
            Functions.showError("Login Filed!", "Wrong username or password");
        }

    }

    private Boolean login(String username, String password) {
        boolean isAuthenticated = false;

        // This is because of empty value
        if(username == null || password == null) {
            return false;
        }

        try {
            FileReader fileReader = new FileReader(DB_PATH);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");


                // For empty save error handling I did this extra condition to assign value in variable
                String _username =  data.length > 0  ? data[0] : "";
                String _password =  data.length > 1  ? data[1] : "";



                if (username.equals(_username) && password.equals(_password)) {
                    isAuthenticated = true;
                    break;
                }


            }

            fileReader.close();
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isAuthenticated;
    }



}
