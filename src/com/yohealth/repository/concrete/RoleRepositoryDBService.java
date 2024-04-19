package com.yohealth.repository.concrete;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.yohealth.domain.model.Role;
import com.yohealth.repository.interfaces.IRoleRepository;
import com.yohealth.utils.*;

public class RoleRepositoryDBService extends BaseRepositoryDBService implements IRoleRepository {

    // Define required queries.
    private String createRoleQuery;
    private String getRoleByIDQuery;
    private String getRoleByNameQuery;

    public RoleRepositoryDBService() {
        super();
        try {
        ReadDbQueries readDbQueries = new ReadDbQueries();
        // Read db queries and map them to private values.
        createRoleQuery = readDbQueries.readSQLQuery("src/sql_queries/Role/createRole.sql");
        getRoleByIDQuery = readDbQueries.readSQLQuery("src/sql_queries/Role/getRoleByID.sql");
        getRoleByNameQuery = readDbQueries.readSQLQuery("src/sql_queries/Role/getRoleByName.sql");
        } catch (Exception e) {
            System.out.println("Error reading SQL queries in RoleRepositoryDbService.");
        }
    }

    @Override
    public Role getRoleByID(long roleId) {
        try (PreparedStatement statement = this.connection.prepareStatement(getRoleByIDQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {  // pass getRoleByIDQuery to the statement object we instantiated with the prepareStatemented method of Connection class
            statement.setLong(1, roleId);
            ResultSet resultSet = statement.executeQuery(); // executes our PreparedStatement object query and returns the data produced by this query(ResultSet)

            if (resultSet.first()) { // checks if the executed query returned a result set by checking first row of result set
                Role role = mapResultSetToRole(resultSet); // instatiate role object with results executed from query
                return role;
            }
        } catch (SQLException e) {
            printSqlException(e, "Error retrieving role by ID in RoleRepositoryDbService.");
        }
        return null; // retunrs NULL if query did not return a result set with rows
    }

    @Override
    public Role getRoleByName(String roleName) {
        try (PreparedStatement statement = this.connection.prepareStatement(getRoleByNameQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) { // pass getRoleByNameQuery to the statement object we instantiated with the prepareStatemented method of Connection class
            statement.setString(1, roleName);
            ResultSet resultSet = statement.executeQuery(); // executes our PreparedStatement object query and returns the data produced by this query(ResultSet)
    
            if (resultSet.first()) { // checks if the executed query returned a result set by checking first row of result set
                Role role = mapResultSetToRole(resultSet); // instatiate role object with results executed from query
                return role;
            }
        } catch (SQLException e) {
            printSqlException(e, "Error retrieving role by name in RoleRepositoryDbService.");
        }
        return null; // retunrs NULL if query did not return a result set with rows
    }

    @Override
    public int createRole(Role role) {
        try (PreparedStatement statement = this.connection.prepareStatement(createRoleQuery)) {  // use the connection object's prepareStatement method to create a PreparedStatement object(statement) with the SQL create role query passed as a parameter
            statement.setLong(1, role.id); // set role ID in PreparedStatement object
            statement.setString(2, role.name); // set role name in PreparedStatement object
            // no non-nullable fields for Role type so no more statement settings needed

            // Execute the create role query and return the number of rows affected
            this.startTransaction(); // start the transaction on (this) RoleRespositoryDBService object
            int rowsAffected = statement.executeUpdate(); // executes our SQL queries set in statement above. returns number of rows we successfully executed
            this.commitTransaction(); // commit the transaction on RoleRespositoryDBService object to the database
            return rowsAffected; // returns number of rows we affected by creating new Role object
        } catch (SQLException e) {
            printSqlException(e, "Error creating role in RoleRepositoryDbService.");
            this.rollbackTransaction();
        }
        return 0; // If nothing is committed, return 0(rows affected), meaning role was not created
    }

    private Role mapResultSetToRole(ResultSet resultSet) throws SQLException { // mapping the query result returned from Role table to a role object to work with in our code :)
        Role role = new Role();
        role.id = (resultSet.getLong("id"));
        role.name = (resultSet.getString("name"));
        return role;
    }
    
}
