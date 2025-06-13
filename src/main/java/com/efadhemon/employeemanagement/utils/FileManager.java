package com.efadhemon.employeemanagement.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private final File file;

    public FileManager(String filePath) {
        file = new File(filePath);
    }

    public List<String> readLines() throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        if (file.exists()) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        br.close();
        return lines;
    }

    public void writeLines(List<String> lines) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for (String line : lines) {
            bw.write(line);
            bw.newLine();
        }
        bw.close();
    }

    public void appendLine(String line) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
        bw.write(line);
        bw.newLine();
        bw.close();
    }
}