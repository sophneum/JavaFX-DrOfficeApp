package com.yohealth.repository.concrete;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.yohealth.repository.interfaces.IVisitRepository;
import java.util.Collection;
import com.yohealth.domain.model.Visit;
import com.yohealth.utils.*;
import java.util.ArrayList;

public class VisitRepositoryDBService extends BaseRepositoryDBService implements IVisitRepository {

    //define SQL Queries
    private DbUtilities dbUtilities;
    private String createVisitQuery;
    private String getAllVisitsByPatientIDQuery;
    private String getVisitByIDQuery;
    private String updateVisitQuery;

    public VisitRepositoryDBService() {
        super();
        try {
            ReadDbQueries readDbQueries = new ReadDbQueries();

            //read and map Queries
            createVisitQuery = readDbQueries.readSQLQuery("src/sql_queries/Visit/createVisit.sql");
            getAllVisitsByPatientIDQuery = readDbQueries.readSQLQuery("src/sql_queries/Visit/getAllVisitsByPatientID.sql");
            getVisitByIDQuery = readDbQueries.readSQLQuery("src/sql_queries/Visit/getVisitByID.sql");
            updateVisitQuery = readDbQueries.readSQLQuery("src/sql_queries/Visit/updateVisit.sql");
        } catch (Exception exc) {

            System.out.println("Error reading SQL queries in VisitRepositoryDbService.");
        }
        dbUtilities = new DbUtilities();
        dbUtilities = new DbUtilities();
    }

    @Override
    public int createVisit(Visit visit) {
        try (PreparedStatement statement = this.connection.prepareStatement(createVisitQuery)) {

            //set statements for all non-nullables
            dbUtilities.setNullableDate(statement,1, DateUtils.convertToSqlDate(visit.date));
            dbUtilities.setNullableString(statement,2, visit.reason); // set to nullable string since nurse creates visit only taking in patient vitals
            dbUtilities.setNullableString(statement,3, visit.summary); // set to nullable string since nurse creates visit without visit summary
            dbUtilities.setNullableString(statement,4, visit.evaluation);// set to nullable string since nurse creates visit without doctor evaluation
            
            statement.setLong(5, visit.patientID);
            
            dbUtilities.setNullableLong(statement,6, visit.doctorID); // set to nullable long since nurse creates visit without doctorID

            
            dbUtilities.setNullableLong(statement,6, visit.doctorID); // set to nullable long since nurse creates visit without doctorID

            statement.setFloat(7, visit.weightInKg);
            statement.setFloat(8, visit.heightInCm);
            statement.setFloat(9, visit.temperatureInF);
            statement.setInt(10, visit.bloodPressureSystolic);
            statement.setInt(11, visit.bloodPressureDiastolic);

            //exec createVisit query // return num Rows affected
            this.startTransaction();
            int rowsAffected = statement.executeUpdate();
            this.commitTransaction();
            return rowsAffected;

        } catch (SQLException exc) {

            printSqlException(exc, "Error creating visit in VisitRepositoryDBService.");
            this.rollbackTransaction();
        }

        return 0;
    }

    @Override
    public Visit getVisitByID(long visitID) {

        try (PreparedStatement statement = this.connection.prepareStatement(getVisitByIDQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            statement.setLong(1, visitID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.first()) {
                Visit visit = mapResultSetToVisit(resultSet);
                return visit;
            }
        } catch (SQLException e) {

            printSqlException(e, "Error retrieving visit by ID in VisitRepositoryDbService.");
        }

    return null;
    }

    @Override
    public Collection<Visit> getAllVisitsByPatientID(long userID) {

        Collection<Visit> visits = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(getAllVisitsByPatientIDQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, userID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Visit visit = mapResultSetToVisit(resultSet);
                visits.add(visit);
            }
        } catch (SQLException exc) {
            printSqlException(exc, "Error retrieving visits by id in VisitRepositoryDbService.");
        }
        return visits;
    }
    
    @Override
    public int updateVisit(Visit visit) {

        try (PreparedStatement statement = this.connection.prepareStatement(updateVisitQuery)) {

            //set statements for all non-nullables
            
            statement.setDate(1, DateUtils.convertToSqlDate(visit.date));
            statement.setString(2, visit.reason);
            statement.setString(3, visit.summary);
            statement.setString(4, visit.evaluation);
            statement.setLong(5, visit.patientID);
            statement.setLong(6, visit.doctorID);
            statement.setFloat(7, visit.weightInKg);
            statement.setFloat(8, visit.heightInCm);
            statement.setFloat(9, visit.temperatureInF);
            statement.setInt(10, visit.bloodPressureSystolic);
            statement.setInt(11, visit.bloodPressureDiastolic);
            statement.setLong(12, visit.id);

            //exec updateVisit query // return num Rows affected
            this.startTransaction();
            int rowsAffected = statement.executeUpdate();
            this.commitTransaction();
            return rowsAffected;

        } catch (SQLException exc) {

            printSqlException(exc, "Error updating visit in VisitRepositoryDBService.");
            this.rollbackTransaction();
        }

        return 0;
    }
    
    private Visit mapResultSetToVisit (ResultSet resultSet) throws SQLException{

        Visit visit = new Visit();
        visit.id = (resultSet.getLong("id"));
        visit.date = (resultSet.getDate("date"));
        visit.reason = (resultSet.getString("reason"));
        visit.summary = (resultSet.getString("summary"));
        visit.evaluation = (resultSet.getString("evaluation"));
        visit.patientID = (resultSet.getLong("patientID"));
        visit.doctorID = (resultSet.getLong("doctorID"));
        visit.weightInKg = (resultSet.getFloat("weightInKg"));
        visit.heightInCm = (resultSet.getFloat("heightInCm"));
        visit.temperatureInF = (resultSet.getFloat("temperatureInF"));
        visit.bloodPressureSystolic = (resultSet.getInt("bloodPressureSystolic"));
        visit.bloodPressureDiastolic = (resultSet.getInt("bloodPressureDiastolic"));
        return visit;
    }
}