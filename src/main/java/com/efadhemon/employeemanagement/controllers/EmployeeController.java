package com.efadhemon.employeemanagement.controllers;


import com.efadhemon.employeemanagement.models.Employee;
import com.efadhemon.employeemanagement.utils.FileManager;
import com.efadhemon.employeemanagement.utils.Functions;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.*;
import java.io.IOException;

public class EmployeeController {

    private static final  String DB_FILE_NAME = "employees.txt";

    @FXML private TableView<Employee> table;
    @FXML private TableColumn<Employee, String> colId;
    @FXML private TableColumn<Employee, String> colName;
    @FXML private TableColumn<Employee, String> colRole;
    @FXML private TableColumn<Employee, Void> colActions;

    private final ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().id));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().name));
        colRole.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().role));
        table.setItems(employeeList);

        loadEmployees();

        // Add Delete & Update Buttons to the Actions Column
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button("Delete");
            private final Button btnUpdate = new Button("Update");
            private final HBox pane = new HBox(5, btnUpdate, btnDelete);

            {
                btnDelete.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    deleteEmployee(emp);
                });

                btnUpdate.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    updateEmployee(emp);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }



    private void loadEmployees() {
        try {
            List<String> lines = FileManager.readLines(DB_FILE_NAME);
            employeeList.clear();
            for (String line : lines) {
                String[] parts = line.split(",");
                employeeList.add(new Employee(parts[0], parts[1], parts[2]));
            }
            table.setItems(employeeList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addEmployee() {
        Optional<Employee> result = showEmployeeDialog(null); // null means add new employee
        result.ifPresent(emp -> {
            try {
                // Check if ID already exists
                List<String> lines = FileManager.readLines("employees.txt");
                boolean exists = lines.stream().anyMatch(line -> line.startsWith(emp.id + ","));
                if (exists) {
                    Functions.showError("Error", "Employee ID already exists!");
                    return;
                }

                // Append new employee
                lines.add(emp.id + "," + emp.name + "," + emp.role);
                FileManager.writeLines("employees.txt", lines);
                loadEmployees();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteEmployee(Employee emp) {
        try {
            List<String> lines = FileManager.readLines(DB_FILE_NAME);
            lines.removeIf(line -> line.startsWith(emp.id + ","));
            FileManager.writeLines(DB_FILE_NAME, lines);
            loadEmployees();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(Employee emp) {
        Optional<Employee> result = showEmployeeDialog(emp); // pass existing employee to update
        result.ifPresent(updatedEmp -> {
            try {
                List<String> lines = FileManager.readLines("employees.txt");
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).startsWith(emp.id + ",")) {
                        lines.set(i, updatedEmp.id + "," + updatedEmp.name + "," + updatedEmp.role);
                    }
                }
                FileManager.writeLines("employees.txt", lines);
                loadEmployees();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Optional<Employee> showEmployeeDialog(Employee emp) {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle(emp == null ? "Add New Employee" : "Update Employee");
        dialog.setHeaderText(emp == null ? "Add a new employee" : "Update details for Employee ID: " + emp.id);

        ButtonType confirmButtonType = new ButtonType(emp == null ? "Add" : "Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtId = new TextField();
        TextField txtName = new TextField();
        TextField txtRole = new TextField();

        if (emp != null) {
            txtId.setText(emp.id);
            txtId.setDisable(true); // Usually ID is not editable during update
            txtName.setText(emp.name);
            txtRole.setText(emp.role);
        }

        grid.add(new Label("ID:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(txtName, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(txtRole, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Enable/disable confirm button depending on input validation (optional)
        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(emp == null); // Disable add button if ID is empty initially

        txtId.textProperty().addListener((obs, oldVal, newVal) -> {
            confirmButton.setDisable(newVal.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Employee(txtId.getText().trim(), txtName.getText().trim(), txtRole.getText().trim());
            }
            return null;
        });

        return dialog.showAndWait();
    }

}