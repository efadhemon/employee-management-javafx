package com.efadhemon.employeemanagement.controllers;


import com.efadhemon.employeemanagement.App;
import com.efadhemon.employeemanagement.models.Employee;
import com.efadhemon.employeemanagement.utils.FileManager;
import com.efadhemon.employeemanagement.utils.Functions;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class EmployeeController {

    private static final String DB_FILE_PATH = "src/databases/employees.txt";
    private static final FileManager fileManager = new FileManager(DB_FILE_PATH);


    private final ObservableList<Employee> data = FXCollections.observableArrayList();
    @FXML
    private TableView<Employee> table;
    @FXML
    private TableColumn<Employee, String> colId;
    @FXML
    private TableColumn<Employee, String> colName;
    @FXML
    private TableColumn<Employee, String> colRole;
    @FXML
    private TableColumn<Employee, Void> colActions;

    public static boolean isExist(String id) {
        try {
            List<String> lines = fileManager.readLines();
            boolean exists = lines.stream().anyMatch(line -> line.startsWith(id + ","));
            return exists;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colRole.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));
        table.setItems(data);

        loadData();

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

    private void loadData() {
        try {
            List<String> lines = fileManager.readLines();
            data.clear();
            for (String line : lines) {
                data.add(new Employee().lineToEmployee(line));
            }
            table.setItems(data);
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
                boolean exists = isExist(emp.getId());
                if (exists) {
                    Functions.showError("Error", "Employee ID already exists!");
                    return;
                }

                List<String> lines = fileManager.readLines();
                // Append new employee
                lines.add(emp.toDBLine());
                fileManager.writeLines(lines);
                loadData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteEmployee(Employee emp) {
        try {
            List<String> lines = fileManager.readLines();
            lines.removeIf(line -> line.startsWith(emp.getId() + ","));
            fileManager.writeLines(lines);
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(Employee emp) {
        Optional<Employee> result = showEmployeeDialog(emp); // pass existing employee to update
        result.ifPresent(updatedEmp -> {
            try {
                List<String> lines = fileManager.readLines();
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).startsWith(emp.getId() + ",")) {
                        lines.set(i, updatedEmp.toDBLine());
                    }
                }
                fileManager.writeLines(lines);
                loadData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Optional<Employee> showEmployeeDialog(Employee emp) {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle(emp == null ? "Add New Employee" : "Update Employee");
        dialog.setHeaderText(emp == null ? "Add a new employee" : "Update details for Employee ID: " + emp.getId());

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
            txtId.setText(emp.getId());
            txtId.setDisable(true); // Usually ID is not editable during update
            txtName.setText(emp.getName());
            txtRole.setText(emp.getRole());
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