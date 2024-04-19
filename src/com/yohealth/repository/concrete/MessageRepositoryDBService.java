package com.yohealth.repository.concrete;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import com.yohealth.domain.model.Message;
import com.yohealth.repository.interfaces.IMessageRepository;
import com.yohealth.utils.*;

public class MessageRepositoryDBService extends BaseRepositoryDBService implements IMessageRepository {

    private String createMessageQuery;
    private String getMessageByIDQuery;
    private String getAllMessagesByUserIDQuery;
    private String updateMessageQuery;

    public MessageRepositoryDBService() {
        super();
        try {
        ReadDbQueries readDbQueries = new ReadDbQueries();
        createMessageQuery = readDbQueries.readSQLQuery("src/sql_queries/Message/createMessage.sql");
        getMessageByIDQuery = readDbQueries.readSQLQuery("src/sql_queries/Message/getMessageByID.sql");
        getAllMessagesByUserIDQuery = readDbQueries.readSQLQuery("src/sql_queries/Message/getAllMessagesByUserID.sql");
        updateMessageQuery = readDbQueries.readSQLQuery("src/sql_queries/Message/updateMessage.sql");


        } catch (Exception e) {
            System.out.println("Error reading SQL queries in MessageRepositoryDBService.");
        }
    }

    @Override
    public int createMessage(Message message) {
        try(PreparedStatement statement = this.connection.prepareStatement(createMessageQuery)) {
            statement.setString(1, message.subject);
            statement.setString(2, message.content);
            statement.setLong(3, message.id);
            statement.setDate(4, DateUtils.convertToSqlDate(message.date));
            statement.setLong(5 , message.patientID);

            this.startTransaction();
            int rowsAffected = statement.executeUpdate();
            this.commitTransaction();
            return rowsAffected;
        } catch (SQLException e) {
            printSqlException(e, "Error creating message in MessageRepositoryDBService.");
            this.rollbackTransaction();
        }
        return 0;
    }

    @Override
    public Message getMessageByID(Long messageID) {
        try(PreparedStatement statement = this.connection.prepareStatement(getMessageByIDQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, messageID);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.first()) {
                Message message = mapResultSetToMessage(resultSet);
                return message;
            }
        } catch (SQLException e) {
            printSqlException(e, "Error retrieving users by name in MessageRepositoryDBService.");
        }
        return null;
    }

    @Override
    public Collection<Message> getAllMessagesByUserID(Long userID) {
        Collection<Message> messages = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(getAllMessagesByUserIDQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, userID);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Message message = mapResultSetToMessage(resultSet);
                messages.add(message);
            }
        } catch (SQLException e) {
            printSqlException(e, "Error retrieving users by name in MessageRepositoryDBService.");
        }
        return messages;
    }

    @Override
    public int updateMessage(Message message) {
        try (PreparedStatement statement = this.connection.prepareStatement(updateMessageQuery)) {
            statement.setString(1, message.subject);
            statement.setString(2, message.content);
            statement.setDate(3, DateUtils.convertToSqlDate(message.date));
            statement.setLong(4 , message.patientID);
            statement.setLong(5, message.id);

            this.startTransaction();
            int rowsAffected = statement.executeUpdate();
            this.commitTransaction();
            return rowsAffected;
                    
        } catch (Exception e) {
            System.out.println("Error reading SQL queries in MessageRepositoryDBService.");
        }
        return 0;
    }

    private Message mapResultSetToMessage(ResultSet resultSet) throws SQLException { // mapping the query result returned from Role table to a role object to work with in our code :)
        Message message = new Message();
        message.id = (resultSet.getLong("id"));
        message.subject = (resultSet.getString("subject"));
        message.content = (resultSet.getString("content"));
        message.date = (resultSet.getDate("date"));
        message.patientID = (resultSet.getLong("patientID"));
        return message;
    }
}
