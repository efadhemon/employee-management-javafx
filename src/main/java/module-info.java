module com.efadhemon.employeemanagement {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.efadhemon.employeemanagement to javafx.fxml;
    exports com.efadhemon.employeemanagement;
}