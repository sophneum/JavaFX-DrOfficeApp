package com.yohealth.domain.config;

import java.sql.Connection;

public class DatabaseConfig {
    private String DB_URL;
    private String DB_USERNAME;
    private String DB_PASSWORD;
    private Connection connection;

    public DatabaseConfig() {
    }
    public String getDB_URL() {
        return DB_URL;
    }
    public void setDB_URL(String DB_URL) {
        this.DB_URL = DB_URL;
    }
    public String getDB_USERNAME() {
        return DB_USERNAME;
    }
    public void setDB_USERNAME(String DB_USERNAME) {
        this.DB_USERNAME = DB_USERNAME;
    }
    public String getDB_PASSWORD() {
        return DB_PASSWORD;
    }
    public void setDB_PASSWORD(String DB_PASSWORD) {
        this.DB_PASSWORD = DB_PASSWORD;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public Connection getConnection() {
        return connection;
    }
}