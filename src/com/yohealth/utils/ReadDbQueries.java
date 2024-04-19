package com.yohealth.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadDbQueries {

    public static class FileReadingException extends Exception {
        public FileReadingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public String readSQLQuery(String filename) throws FileReadingException {
        StringBuilder query = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                query.append(line).append(" ");
            }
        } catch (IOException e) {
            String errorMessage = String.format("Failed to read from file: %s. Error details: %s", filename, e.getMessage());
            System.err.println(errorMessage);

            throw new FileReadingException("Failed to read SQL query from file: " + filename, e);
        }
        return query.toString();
    }
}
