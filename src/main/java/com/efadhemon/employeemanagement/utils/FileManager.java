package com.efadhemon.employeemanagement.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String FILES_ROOT_PAT = "src/databases/";

    public static List<String> readLines(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        File file = new File(FILES_ROOT_PAT + filename);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            }
        }
        return lines;
    }

    public static void writeLines(String filename, List<String> lines) throws IOException {
        File file = new File(FILES_ROOT_PAT + filename);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    public static void appendLine(String filename, String line) throws IOException {
        File file = new File(FILES_ROOT_PAT + filename);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(line);
            bw.newLine();
        }
    }
}