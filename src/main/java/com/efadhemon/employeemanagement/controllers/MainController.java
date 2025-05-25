package com.efadhemon.employeemanagement.controllers;


import com.efadhemon.employeemanagement.App;
import com.efadhemon.employeemanagement.utils.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class MainController {
    public void logout(ActionEvent event) throws Exception {
        Session.currentUser = null;
        Session.isAuthenticated = false;

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader  fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 586, 586);
        stage.setScene(scene);
    }

    public void manageEmployees(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("employee.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
        stage.setScene(scene);
    }

    public void manageAttendance(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("attendance.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 586, 586);
        stage.setScene(scene);
    }
}