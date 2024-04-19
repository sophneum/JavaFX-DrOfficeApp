package com.yohealth.repository.concrete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.yohealth.utils.ReadDbQueries;
import com.yohealth.domain.config.DatabaseConfig;

public class BaseRepositoryDBService {
    protected Connection connection;

    private String createDbSql;
    private String useDbSql;
    private String createRoleSql;
    private String createUserSql;
    private String createMessageSql;
    private String createPrescriptionSql;
    private String createVisitSql;
    private String seedRolesSql;

    public BaseRepositoryDBService() {
        loadCreateStatements();
        DatabaseConfig config = getDatabaseConfig();
        this.connection = config.getConnection();
        testConnection();
    }

    private void loadCreateStatements() {
        try {
            ReadDbQueries readDbQueries = new ReadDbQueries();
            createDbSql = readDbQueries.readSQLQuery("src/sql_queries/createDatabase.sql");
            useDbSql = readDbQueries.readSQLQuery("src/sql_queries/useDatabase.sql");
            createRoleSql = readDbQueries.readSQLQuery("src/sql_queries/createRoleTable.sql");
            createUserSql = readDbQueries.readSQLQuery("src/sql_queries/createUserTable.sql");
            createMessageSql = readDbQueries.readSQLQuery("src/sql_queries/createMessageTable.sql");
            createPrescriptionSql = readDbQueries.readSQLQuery("src/sql_queries/createPrescriptionTable.sql");
            createVisitSql = readDbQueries.readSQLQuery("src/sql_queries/createVisitTable.sql");
            seedRolesSql = readDbQueries.readSQLQuery("src/sql_queries/seedRoles.sql");
        } catch (ReadDbQueries.FileReadingException e) {
            System.err.println("Error loading SQL queries from file.");
        }
    }

    private DatabaseConfig getDatabaseConfig() {
        DatabaseConfig config = new DatabaseConfig();
        config.setDB_URL("jdbc:mysql://server.local/yohealth_dev");
        config.setDB_USERNAME("yohealthdev");
        config.setDB_PASSWORD("storingpasswordsinappisaterriblEidea");

    try {
        Connection connection = DriverManager.getConnection(
                config.getDB_URL(),
                config.getDB_USERNAME(),
                config.getDB_PASSWORD()
        );
        config.setConnection(connection);
    } catch (SQLException e) {
        printSqlException(e, "Failed to establish a database connection.");
    }
        return config;
    }

    protected void testConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                createDatabase();
                DatabaseConfig config = getDatabaseConfig();
                this.connection = config.getConnection();
            }
        } catch (SQLException e) {
            printSqlException(e, "Error testing database connection.");
        }
    }

    private void createDatabase() {
        try (Connection tempConnection = DriverManager.getConnection(
                "jdbc:mysql://yohealth-dev.cluster-cx00ki6cc44e.us-east-2.rds.amazonaws.com",
                "sneums",
                "goodbyeandfarewall")) {

            tempConnection.setAutoCommit(false);

            // Create the database
            try (PreparedStatement statement = tempConnection.prepareStatement(createDbSql)) {
                statement.execute();
            }

            // Use the newly created database
            try (PreparedStatement statement = tempConnection.prepareStatement(useDbSql)) {
                statement.execute();
            }

            // Create the tables
            try (PreparedStatement statement = tempConnection.prepareStatement(createRoleSql)) {
                statement.execute();
            }
            try (PreparedStatement statement = tempConnection.prepareStatement(createUserSql)) {
                statement.execute();
            }
            try (PreparedStatement statement = tempConnection.prepareStatement(createMessageSql)) {
                statement.execute();
            }
            try (PreparedStatement statement = tempConnection.prepareStatement(createPrescriptionSql)) {
                statement.execute();
            }
            try (PreparedStatement statement = tempConnection.prepareStatement(createVisitSql)) {
                statement.execute();
            }
            
            // Seed the roles table
            try (PreparedStatement statement = tempConnection.prepareStatement(seedRolesSql)) {
                statement.execute();
            }

            tempConnection.commit();
        } catch (SQLException e) {
            printSqlException(e, "Error creating database and tables.");
        }
    }

    protected void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                printSqlException(e, "Error closing connection to remote SQL server.");
            }
        }
    }

    protected void rollbackTransaction() {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                printSqlException(e, "Error rollingback transaction on remote SQL server.");
                System.err.println("Potential data corruption, validate database system and restore from backup if needed.");
            }
        }
    }

    protected void commitTransaction() {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                printSqlException(e, "Error committing transaction on remote SQL server.");
            }
        }
    }

    protected void startTransaction() {
        if (connection != null) {
            try {
                connection.setAutoCommit(false); // We will explicity commit transactions by calling commitTransaction as needed.
            } catch (SQLException e) {
                printSqlException(e, "Error starting transaction on remote SQL server.");
            }
        }
    }

    protected void printSqlException(SQLException e, String message) {
        System.err.println(message);
        System.err.println("Error Code: " + e.getErrorCode());
        System.err.println("SQL State: " + e.getSQLState());
        System.err.println("Message: " + e.getMessage());
        System.err.println("Cause: " + e.getCause());
    }
}