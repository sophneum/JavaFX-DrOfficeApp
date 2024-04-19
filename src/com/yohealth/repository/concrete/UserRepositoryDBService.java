package com.yohealth.repository.concrete;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import com.yohealth.domain.model.User;
import com.yohealth.repository.interfaces.IUserRepository;  
import com.yohealth.utils.*;
import java.util.Date;

public class UserRepositoryDBService extends BaseRepositoryDBService implements IUserRepository {
    
    // Define required queries.
    private String createUserQuery;
    private String getUserByIdQuery;
    private String updateUserQuery;
    private String getUserByUsernameQuery;
    private String getUsersByNameQuery;
    private String getUsersByRoleIDQuery; 
    private String getUsersByDOBQuery;
    private String getAllUsersQuery;
    private String getUsersByNameAndRoleIDQuery;

    private DbUtilities dbUtilities;

    public UserRepositoryDBService() {
        super();
        try {
            ReadDbQueries readDbQueries = new ReadDbQueries();
            // Read db queries and map them to private values.
            createUserQuery = readDbQueries.readSQLQuery("src/sql_queries/User/createUser.sql");
            getUserByIdQuery = readDbQueries.readSQLQuery("src/sql_queries/User/getUserById.sql");
            updateUserQuery = readDbQueries.readSQLQuery("src/sql_queries/User/updateUser.sql");
            getUserByUsernameQuery = readDbQueries.readSQLQuery("src/sql_queries/User/getUserByUsername.sql");
            getUsersByNameQuery = readDbQueries.readSQLQuery("src/sql_queries/User/getUsersByName.sql");
            getUsersByRoleIDQuery = readDbQueries.readSQLQuery("src/sql_queries/User/getUsersByRoleID.sql");
            getUsersByDOBQuery = readDbQueries.readSQLQuery("src/sql_queries/User/getUsersByDOB.sql");
            getAllUsersQuery = readDbQueries.readSQLQuery("src/sql_queries/User/getAllUsers.sql");
            getUsersByNameAndRoleIDQuery = readDbQueries.readSQLQuery("src/sql_queries/User/getUsersByNameAndRoleID.sql");
        } catch (Exception e) {
            System.out.println("Error reading SQL queries in UserRepositoryDbService.");
        }
        dbUtilities = new DbUtilities();
    }

    @Override
    public int updateUser(User user) {
        try (PreparedStatement statement = this.connection.prepareStatement(updateUserQuery)) {
            // Set the parameter values for non-nullable fields
            statement.setString(1, user.name);
            statement.setString(2, user.username);
            statement.setLong(3, user.roleID);
            statement.setString(4, user.email);
            statement.setString(7, user.password);

            // Set the parameter values for nullable fields
            dbUtilities.setNullableString(statement, 5, user.phone);
            dbUtilities.setNullableString(statement, 6, user.emergencyContactPhone);
            dbUtilities.setNullableString(statement, 8, user.mailingAddress);
            dbUtilities.setNullableString(statement, 9, user.billingAddress);
            dbUtilities.setNullableDate(statement, 10, DateUtils.convertToSqlDate(user.dob));
            dbUtilities.setNullableString(statement, 11, user.insuranceInfo);
            dbUtilities.setNullableString(statement, 12, user.pharmacyInfo);
            dbUtilities.setNullableString(statement, 13, user.allergies);
            dbUtilities.setNullableString(statement, 14, user.immunizations);

            statement.setLong(15, user.id);

            // Check the number of rows affected to determine if the update was successful and return the count.
            this.startTransaction();
            System.out.println(statement);

            int rowsAffected = statement.executeUpdate();
            this.commitTransaction();

            return rowsAffected;

        } catch (SQLException e) {
            printSqlException(e, "Error updating user in UserRepositoryDbService.");
            this.rollbackTransaction();
        }
        return 0;
    }

    @Override
    public int createUser(User user) {
        try (PreparedStatement statement = this.connection.prepareStatement(createUserQuery)) {
            // Set the parameter values for non-nullable fields
            statement.setString(1, user.name);
            statement.setString(2, user.username);
            statement.setLong(3, user.roleID);
            statement.setString(4, user.email);
            statement.setString(5, "yohealth1"); // Set default password
    
            // Set the parameter values for nullable fields
            dbUtilities.setNullableString(statement, 6, user.phone);
            dbUtilities.setNullableString(statement, 7, user.emergencyContactPhone);
            dbUtilities.setNullableString(statement, 8, user.mailingAddress);
            dbUtilities.setNullableString(statement, 9, user.billingAddress);
            dbUtilities.setNullableDate(statement, 10, DateUtils.convertToSqlDate(user.dob));
            dbUtilities.setNullableString(statement, 11, user.insuranceInfo);
            dbUtilities.setNullableString(statement, 12, user.pharmacyInfo);
            dbUtilities.setNullableString(statement, 13, user.allergies);
            dbUtilities.setNullableString(statement, 14, user.immunizations);
    
            // Execute the create user query and return the number of rows affected
            this.startTransaction();
            int rowsAffected = statement.executeUpdate();
            this.commitTransaction();
            return rowsAffected;
        } catch (SQLException e) {
            printSqlException(e, "Error creating user in UserRepositoryDbService.");
            this.rollbackTransaction();
        }
        return 0;
    }

    @Override
    public User getUserById(long userId) {
        try (PreparedStatement statement = this.connection.prepareStatement(getUserByIdQuery,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.first()) {
                User user = mapResultSetToUser(resultSet);
                return user;
            }
        } catch (SQLException e) {
            printSqlException(e, "Error retrieving user by ID in UserRepositoryDbService.");
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        try (PreparedStatement statement = this.connection.prepareStatement(getUserByUsernameQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.first()) {
                User user = mapResultSetToUser(resultSet);
                return user;
            }
        } catch (SQLException e) {
            printSqlException(e, "Error retrieving users by username in UserRepositoryDbService.");
        }
        return null;
    }

    @Override
    public Collection<User> getUsersByName(String name) {
        Collection<User> users = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(getUsersByNameQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            printSqlException(e, "Error retrieving users by name in UserRepositoryDbService.");
        }
        return users;
    }

    @Override
    public Collection<User> getUsersByRoleID(int roleID) {
        Collection<User> users = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(getUsersByRoleIDQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, roleID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            printSqlException(e, "Error retrieving users by roleID in UserRepositoryDbService.");
        }
        return users;
    }

    @Override
    public Collection<User> getUsersByNameAndRoleID(String name, int roleID) {
        Collection<User> users = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(getUsersByNameAndRoleIDQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, name);
            statement.setLong(2, roleID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            printSqlException(e, "Error retrieving users by name and roleID in UserRepositoryDbService.");
        }
        return users;
    }

    @Override
    public Collection<User> getUsersByDOB(Date dob) {
        Collection<User> users = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(getUsersByDOBQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setDate(1, new java.sql.Date(dob.getTime()));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            printSqlException(e, "Error retrieving users by DOB in UserRepositoryDbService.");
        }
        return users;
    }

    @Override
    public Collection<User> getAllUsers() {
        Collection<User> users = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(getAllUsersQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            printSqlException(e, "Error retrieving all users in UserRepositoryDbService.");
        }
        return users;
    }

    // Centralize User mapping as it is used across all read operations.
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.id = (resultSet.getLong("id"));
        user.name = (resultSet.getString("name"));
        user.username = (resultSet.getString("username"));
        user.roleID = (resultSet.getLong("roleID"));
        user.email = (resultSet.getString("email"));
        user.phone = (resultSet.getString("phone"));
        user.emergencyContactPhone = (resultSet.getString("emergencyContactPhone"));
        user.password = (resultSet.getString("password"));
        user.mailingAddress = (resultSet.getString("mailingAddress"));
        user.billingAddress = (resultSet.getString("billingAddress"));
        user.dob = (resultSet.getDate("dob"));
        user.insuranceInfo = (resultSet.getString("insuranceInfo"));
        user.pharmacyInfo = (resultSet.getString("pharmacyInfo"));
        user.allergies = (resultSet.getString("allergies"));
        user.immunizations = (resultSet.getString("immunizations"));
        return user;
    }
}