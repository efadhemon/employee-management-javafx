package com.efadhemon.employeemanagement.controllers;

import com.efadhemon.employeemanagement.App;
import com.efadhemon.employeemanagement.models.Attendance;
import com.efadhemon.employeemanagement.utils.FileManager;
import com.efadhemon.employeemanagement.utils.Functions;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AttendanceController {

    private final String DB_FILE_PATH = "src/databases/attendance.txt";
    private final FileManager fileManager = new FileManager(DB_FILE_PATH);

    private final ObservableList<Attendance> data = FXCollections.observableArrayList();

    @FXML
    private TableView<Attendance> table;

    @FXML
    private ComboBox<String> employeeId;

    @FXML
    private TableColumn<Attendance, String> colId;
    @FXML
    private TableColumn<Attendance, String> colDate;
    @FXML
    private TableColumn<Attendance, String> colCheckIn;
    @FXML
    private TableColumn<Attendance, String> colCheckOut;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().employeeId));
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().date));
        colCheckIn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().checkIn));
        colCheckOut.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().checkOut));

        table.setItems(data);


        loadData();
        loadEmployeeIds();
    }

    @FXML
    public void handleCheckIn() {
        String empId = employeeId.getValue();
        if (empId == null || empId.trim().isEmpty()) {
            Functions.showError("Error", "Please select an Employee ID.");
            return;
        }

        boolean isEmployeeExists = EmployeeController.isExist(empId);
        if (!isEmployeeExists) {
            Functions.showError("Error", "Employee ID does not exist");
            return;
        }

        String date = LocalDate.now().toString();
        String time = LocalTime.now().toString().substring(0, 8);

        try {
            List<String> lines = fileManager.readLines();
            boolean alreadyCheckedIn = lines.stream().anyMatch(line ->
                    line.startsWith(empId + "," + date + ",") && !line.split(",")[2].isEmpty()
            );

            if (alreadyCheckedIn) {
                Functions.showError("Error", "Already checked in today");
                return;

            }

            lines.add(empId + "," + date + "," + time + ",");
            fileManager.writeLines(lines);
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        employeeId.setValue(null);
    }

    @FXML
    public void handleCheckOut() {
        String empId = employeeId.getValue();
        if (empId == null || empId.trim().isEmpty()) {
            Functions.showError("Error", "Please select an Employee ID.");
            return;
        }

        boolean isEmployeeExists = EmployeeController.isExist(empId);
        if (!isEmployeeExists) {
            Functions.showError("Error", "Employee ID does not exist");
            return;
        }

        String date = LocalDate.now().toString();
        System.out.println(date);
        String time = LocalTime.now().toString().substring(0, 8);

        try {
            List<String> lines = fileManager.readLines();

            boolean alreadyCheckedIn = lines.stream().anyMatch(line ->
                    line.startsWith(empId + "," + date + ",") && !line.split(",")[2].isEmpty()
            );

            if (!alreadyCheckedIn) {
                Functions.showError("Error", "Not checked in today");
                return;
            }

            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts[0].equals(empId) && parts[1].equals(date)) {
                    if (parts.length > 2) {
                        lines.set(i, parts[0] + "," + parts[1] + "," + parts[2] + "," + time);
                        break;
                    }
                }
            }

            fileManager.writeLines(lines);
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        employeeId.setValue(null);
    }

    private void loadEmployeeIds() {
        try {
            FileManager empFileManager = new FileManager("src/databases/employees.txt");
            List<String> lines = empFileManager.readLines();

            employeeId.getItems().clear();
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    employeeId.getItems().add(parts[0]); // Employee ID
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadData() {
        try {
            List<String> lines = fileManager.readLines();
            data.clear();
            for (String line : lines) {
                data.add(new Attendance().lineToAttendance(line));
            }
            table.setItems(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            Stage stage = (Stage) table.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 586, 586);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
