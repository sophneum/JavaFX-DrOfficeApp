package com.yohealth.repository.concrete;

import com.yohealth.repository.interfaces.IPrescriptionRepository;
import com.yohealth.domain.model.Prescription;
import java.util.Collection;
import com.yohealth.utils.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrescriptionRepositoryDBService extends BaseRepositoryDBService implements IPrescriptionRepository {
    
    // define required queries
    private String getPrescriptionByIdQuery;
    private String createPrescriptionQuery;
    private String updatePrescriptionQuery;
    private String getAllPrescriptionsByPatientIdQuery;

    public PrescriptionRepositoryDBService() {
        super();
        try { 
            ReadDbQueries readDbQueries = new ReadDbQueries();
            // Read db queries and map them to private values.
            getPrescriptionByIdQuery = readDbQueries.readSQLQuery("src/sql_queries/Prescription/getPrescriptionById.sql");
            createPrescriptionQuery = readDbQueries.readSQLQuery("src/sql_queries/Prescription/createPrescription.sql");
            updatePrescriptionQuery = readDbQueries.readSQLQuery("src/sql_queries/Prescription/updatePrescription.sql");
            getAllPrescriptionsByPatientIdQuery = readDbQueries.readSQLQuery("src/sql_queries/Prescription/getAllPrescriptionsByPatientId.sql");
        } catch (Exception e) {
            System.out.println("Error reading SQL queries in PrescriptionRepositoryDbService.");
        }
    }

    @Override
    public int createPrescription(Prescription prescription) {
        try (PreparedStatement statement = this.connection.prepareStatement(createPrescriptionQuery)) {
            // Set the parameter values for fields
            statement.setString(1, prescription.name);
            statement.setString(2, prescription.dose);
            statement.setDate(3, DateUtils.convertToSqlDate(prescription.startDate));
            statement.setLong(4, prescription.patientID);
        
            // Execute creat prescription query and return num rows affected
            this.startTransaction();
            int numRowsAffected = statement.executeUpdate();
            this.commitTransaction();
            return numRowsAffected;
        } catch (SQLException e) {
            printSqlException(e, "Error creating prescription in PrescriptionRepositoryDBService.");
            this.rollbackTransaction();
        }
        return 0;
    }

    @Override
    public Prescription getPrescriptionById(long prescriptionId) {
        try (PreparedStatement statement = this.connection.prepareStatement(getPrescriptionByIdQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, prescriptionId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Prescription prescription = mapResultSetToPrescription(resultSet);
                return prescription;
            }
        } catch (SQLException e) {
            printSqlException(e, "Error getting prescription by id in PrescriptionRepositoryDBService.");
        }
        return null;
    }

    @Override
    public Collection<Prescription> getAllPrescriptionsByPatientId(long userId) {
        Collection<Prescription> prescriptions = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(getAllPrescriptionsByPatientIdQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Prescription prescription = mapResultSetToPrescription(resultSet);
                prescriptions.add(prescription);
            }
        } catch (SQLException e) {
            printSqlException(e, "Error getting all prescriptions by patient id in PrescriptionRepositoryDBService.");
        }
        return prescriptions;
    }

    @Override
    public int updatePrescription(Prescription prescription) {
        try (PreparedStatement statement = this.connection.prepareStatement(updatePrescriptionQuery)) {
            // Set the parameter values for fields
            statement.setString(1, prescription.name);
            statement.setString(2, prescription.dose);
            statement.setDate(3, DateUtils.convertToSqlDate(prescription.startDate));
            statement.setLong(4, prescription.patientID);
            statement.setLong(5, prescription.id);

            // Execute update prescription query and return num rows affected
            this.startTransaction();
            int numRowsAffected = statement.executeUpdate();
            this.commitTransaction();
            return numRowsAffected;

        } catch (SQLException e) {
            printSqlException(e, "Error updating prescription in PrescriptionRepositoryDBService.");
            this.rollbackTransaction();
        }
        return 0;
    }

    // centralize Prescription mapping used across read operrations (get methods)
    private Prescription mapResultSetToPrescription(ResultSet resultSet) throws SQLException {
        Prescription prescription = new Prescription();
        prescription.id = (resultSet.getLong("id"));
        prescription.name = (resultSet.getString("name"));
        prescription.dose = (resultSet.getString("dose"));
        prescription.startDate = (resultSet.getDate("startDate"));
        prescription.patientID = (resultSet.getLong("patientID"));
        return prescription;
    }

}
