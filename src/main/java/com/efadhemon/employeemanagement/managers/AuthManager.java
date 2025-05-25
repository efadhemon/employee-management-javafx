package com.efadhemon.employeemanagement.managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AuthManager {
    private final  String USERS_DB_PATH = "src/databases/users.txt";

    public Boolean login(String username, String password) {
        boolean isAuthenticated = false;

        // This is because of empty value
        if(username == null || password == null) {
            return false;
        }

        try {
            FileReader fileReader = new FileReader(USERS_DB_PATH);
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