package com.yohealth.controllers;

import com.yohealth.domain.model.*;
import com.yohealth.utils.DateUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PatientController extends BaseController {
    // Main UI
    private String patientHomePage = "/com/resources/views/PatientHomePage.fxml";
    private String patientVisitDetails = "/com/resources/views/PatientVisitDetails.fxml";
    // Message UIs
    private String patientMessageStyle = "/com/resources/views/PatientMessageStyle.fxml";
    private String patientViewMessageStyle = "/com/resources/views/PatientViewMessageStyle.fxml";
    private String patientNewViewMessageStyle = "/com/resources/views/PatientNewMessageStyle.fxml";
    // View Visit UIs
    private String patientViewDetails = "/com/resources/views/PatientViewDetails.fxml";
    private String patientSpecificVisitDetails = "/com/resources/views/PatientSpecificVisitDetails";

    private Collection<Message> patientMessages;
    private Collection<Visit> patientVisits;

    private User loadPatient;
    
    public PatientController () {
        super();
    }

    // patientHomePage view
    @FXML
    private Label welcomePatient;
    @FXML
    private TextField issuesAddressed;
    @FXML
    private TextField doctorName;
    @FXML
    private TextField messageSubject;
    @FXML
    private Button editInfoButtonClicked;
    @FXML
    private Button saveInfoButtonClicked;
    @FXML
    private TextField name;
    @FXML
    private TextField dob;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField mailingAddress;
    @FXML
    private TextField billingAddress;
    @FXML
    private Button viewMessageButtonClicked;
    @FXML
    private Button newPatientMessageButtonClicked;
    @FXML
    private Button universalBackButton;
    @FXML
    private Button universalLogoutButton;
    @FXML
    private TextField emergencyNumber;

    // Message System
    @FXML
    private Label date;
    @FXML
    private Label messageName;
    @FXML
    private Label subject;
    @FXML
    private Button viewButtonMessageStyle;
    @FXML
    private VBox messageContainer;
    @FXML
    private ScrollPane messageScrollPane;
    @FXML
    private Label content;
    @FXML
    private Button backButtonMessageStyle;
    @FXML
    private Label messageID;

    // Create new button
    @FXML
    private Button newMessageButton;
    @FXML
    private Button sendButtonMessageStyle;
    @FXML
    private TextField enterSubject;
    @FXML
    private TextField enterContent;
    @FXML
    private VBox vbox;
    @FXML
    private Label currDate;

    // Visit System
    @FXML
    private Button selectVisitButtonClicked;
    @FXML
    private ScrollPane visitScrollPane;
    @FXML
    private VBox visitContainer;
    @FXML
    private Label visitDate;
    @FXML
    private Label issues;
    @FXML
    private Label visitID;
    @FXML
    private TextArea summary;
    @FXML
    private TextArea evaluation;
    
    @FXML
    private Label summaryLabel;
    @FXML
    private Label evalLabel;

    // ELLA TO ADD ER NUMBER TO INFO
    // ELLA TO FIGURE OUT WHY MESSAGE CONTENT WON'T WRAP TEXT
    // ELLA TO MOVE DOWN PERSONAL INFO BOX A BIT

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            loadPatient = this.userService.getUserById(this.userPersistenceService.getUserPersistence().getUserId());
            //load all the messages
            this.patientMessages = this.messageService.getAllMessagesByUserID(this.userPersistenceService.getUserPersistence().getUserId());
            this.patientVisits = this.visitService.getAllVisitsByPatientID(this.userPersistenceService.getUserPersistence().getUserId());

            if(loadPatient == null) return;

            if(welcomePatient != null && loadPatient.name != null) {
                welcomePatient.setText(("Welcome " + loadPatient.name + "!"));
            }
            if(name != null && loadPatient.name != null) {
                name.setText((loadPatient.name));
            }
            try{
                if(dob != null && loadPatient.dob != null) {
                    dob.setText(DateUtils.dateToPrettyString(loadPatient.dob));
                }
            }catch(Exception e){
                // do nothing
            }
            if(phoneNumber != null && loadPatient.phone != null) {
                phoneNumber.setText(loadPatient.phone);
            }
            if(mailingAddress != null && loadPatient.mailingAddress != null) {
                mailingAddress.setText(loadPatient.mailingAddress);
            }
            if(billingAddress != null && loadPatient.billingAddress != null) {
                billingAddress.setText(loadPatient.billingAddress);
            }
            if(emergencyNumber != null && loadPatient.emergencyContactPhone != null) {
                emergencyNumber.setText(loadPatient.emergencyContactPhone);
            }
            try {
            if(patientMessages != null) {
                populateMessage(patientMessages, messageContainer);
            }
            if(patientVisits != null) {
                populateVisits(patientVisits, visitContainer);
            }
            } catch (Exception e) {
            }
            if(patientMessages == null) {
                System.out.println("Failed to load patient messages.");
            }
            if(patientVisits == null) {
                System.out.println("Failed to load patient visits");
            }
            
        });
    }
    
    public void loadViewPatientHomePage() {
        try {
            URL fxmlUrl = getClass().getResource(patientHomePage);
            if(fxmlUrl == null) {
                throw new FileNotFoundException("PatientHomePage FXML file not found");
            }
            createStage(fxmlUrl);
            this.userPersistence.setPreviousPage(patientHomePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadViewPatientVisitDetails(long visitID) {
        try {
            URL fxmlUrl = getClass().getResource(patientVisitDetails);
            if(fxmlUrl == null) {
                throw new FileNotFoundException("PatientVisitDetails FXML file not found");
            }
            createStage(fxmlUrl);
            this.userPersistence.setPreviousPage(patientHomePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onEditInfoButtonClicked() {
        name.setEditable(true);
        dob.setEditable(true);
        phoneNumber.setEditable(true);
        mailingAddress.setEditable(true);
        billingAddress.setEditable(true);
        emergencyNumber.setEditable(true);
    }

    @FXML
    private void onSaveInfoButtonClicked() {
        name.setEditable(false);
        dob.setEditable(false);
        phoneNumber.setEditable(false);
        mailingAddress.setEditable(false);
        billingAddress.setEditable(false);
        emergencyNumber.setEditable(false);
        updatePatientInfo();;
    }


    @FXML
    private void updatePatientInfo() {
        try{
            if(DateUtils.prettyStringToDate(dob.getText()) != null) {
                loadPatient.dob = DateUtils.prettyStringToDate(dob.getText()); // convert string date back to date object
            }
        }catch(Exception e){
            // do nothing :)
        }
        loadPatient.mailingAddress = mailingAddress.getText();
        loadPatient.phone = phoneNumber.getText();
        loadPatient.billingAddress = billingAddress.getText();
        loadPatient.name = name.getText();
        loadPatient.emergencyContactPhone = emergencyNumber.getText();
        this.userService.updateUser(loadPatient);
    }

    private void populateMessage(Collection<Message> messages, VBox messageBox) {
        ScrollPane scrollPane =null;
        try{
            scrollPane = (ScrollPane)(messageScrollPane).lookup("#messageScrollPane");
        }
        catch(Exception e){
        }

        try{
        VBox messageContainer = null;
        if(scrollPane != null) {
            messageContainer =(VBox) scrollPane.getContent();
        }
        if(messageContainer == null) {
            messageContainer = messageBox;
        }
        if(messageContainer != null) {
            messageContainer.getChildren().clear();
            for(Message message: messages) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(patientMessageStyle));
                    VBox messageView = loader.load();
                    Label date = (Label)messageView.lookup("#date");
                    Label messageName = (Label)messageView.lookup("#messageName");
                    Label subject = (Label)messageView.lookup("#subject");
                    Label messageID = (Label)messageView.lookup("#messageID");
                    date.setText("Date: " + message.date);
                    messageName.setText("Patient Name: " + loadPatient.name);
                    subject.setText("Message Subject: " + message.subject);
                    messageID.setText(Long.toString(message.id));
                    messageContainer.getChildren().add(messageView);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        }
        catch(Exception e) {
        }
    }

    @FXML
    private void onviewMessageButtonClicked(ActionEvent event) throws IOException {
        Button button = (Button)event.getSource();

        VBox messageContainer =(VBox)button.getParent();
        VBox messagesList = (VBox)messageContainer.getParent();
        
        Label messageID = (Label)messageContainer.lookup("#messageID");
       // Message message = this.messageService.getMessageByID(messageID);
        long messageLong = Long.parseLong(messageID.getText());

        if(messageID != null) {
            Message message = this.messageService.getMessageByID(messageLong);
            messagesList.getChildren().clear();
            viewMessageStage(message, messagesList);
        }
    }

    private void viewMessageStage(Message message, VBox messageBox) {
        if(message != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(patientViewMessageStyle));
                VBox messageView = loader.load();

                Label date = (Label)messageView.lookup("#date");
                Label messageName = (Label)messageView.lookup("#messageName");
                Label subject = (Label)messageView.lookup("#subject");
                Label content = (Label)messageView.lookup("#content");
                date.setText("Date: " + message.date);
                messageName.setText("Patient Name: " + loadPatient.name);
                subject.setText("Message Subject: " + message.subject);
                content.setText("Message Content: " + message.content);
                messageBox.getChildren().add(messageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onbackButtonMessageStyle(ActionEvent event) throws IOException {
        loadViewPatientHomePage();
    }

    @FXML
    private void onSendButtonMessageStyle() throws IOException {
        
        String sumtin = enterContent.getText();
        String sumthin = enterSubject.getText();
        if(sumtin.isEmpty() || sumthin.isEmpty() || sumtin == null || sumthin == null){
            enterContent.setText("Must enter a valid body message");
            enterSubject.setText("Must enter a valid subject");
            return;
         }

        Message newMessage = new Message();
        newMessage.content = enterContent.getText();
        newMessage.subject = enterSubject.getText();
        newMessage.date = new Date();
        newMessage.patientID = userPersistence.getUserId();
        messageService.createMessage(newMessage);

        enterSubject.setText("");;
        enterContent.setText("");
        System.out.println("Message sent");
    }

    @FXML
    private void onNewPatientMessageButtonClicked(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        VBox rootVBox = (VBox) button.getParent(); // buttons parent is main vbox
        VBox vbox = (VBox) rootVBox.lookup("#vbox"); // Get the VBox with fx:id 'vbox'
    
        if (vbox != null) {
            ScrollPane scrollPane = (ScrollPane) vbox.lookup("#messageScrollPane"); // Get the ScrollPane with fx:id 'messageScrollPane'
            if (scrollPane != null) {
                VBox messageContainer = (VBox) scrollPane.getContent(); // Get the content of the ScrollPane
                if (messageContainer != null) {
                    System.out.println("Found messageContainer VBox");
                    messageContainer.getChildren().clear();
                    newMessageStage(messageContainer);
                    // Now you have access to the messageContainer VBox
                } else {
                    System.out.println("messageContainer VBox not found");
                }
            } else {
                System.out.println("ScrollPane not found");
            }
        } else {
            System.out.println("vbox VBox not found");
        }
    }

    
    private void newMessageStage(VBox messageBox) throws IOException {
        VBox messageView = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(patientNewViewMessageStyle));
            messageView = loader.load();
        
            Label subject = (Label)messageView.lookup("#subject");
            Label content = (Label)messageView.lookup("#content");

            // hide and disable new message function
            newPatientMessageButtonClicked.setVisible(false);
            newPatientMessageButtonClicked.setDisable(true);

            subject.setText("Subject");
            content.setText("Content");
            messageBox.getChildren().add(messageView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // VISIT TIMEEE
    private void populateVisits(Collection<Visit> visits, VBox viewBox) {
        ScrollPane scrollPane = (ScrollPane)(visitScrollPane).lookup("#visitScrollPane");
        VBox visitContainer = null;
        if(scrollPane != null) {
            visitContainer =(VBox) scrollPane.getContent();

        }
        if(visitContainer == null) {
            visitContainer = viewBox;
        }
        if(visitContainer != null) {
            visitContainer.getChildren().clear();

            for(Visit visit: visits) {
                try {
                
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(patientViewDetails));
                    VBox visitView = loader.load();
                    Label visitDate = (Label)visitView.lookup("#visitDate");
                    // Label provider = (Label)visitView.lookup("#provider");
                    Label issues = (Label)visitView.lookup("#issues");
                    Label visitID = (Label)visitView.lookup("#visitID");
                    visitDate.setText("Date: " + visit.date);

                    issues.setText("Issues Addressed: " + visit.reason);
                    visitID.setText(Long.toString(visit.id));
                    visitContainer.getChildren().addAll(visitView);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void onSelectVisitButton(ActionEvent event) throws IOException {
        Button button = (Button)event.getSource();
        VBox visitContainer = (VBox)button.getParent();
        VBox visitList = (VBox)visitContainer.getParent();

        Label visitID = (Label)visitContainer.lookup("#visitID");
        long visitLong = Long.parseLong(visitID.getText());

        if(visitID != null) {
            Visit visit = this.visitService.getVisitByID(visitLong);
            visitList.getChildren().clear();
            viewSelectedVisits(visit, visitList);
        }
    }

    private void viewSelectedVisits(Visit visit, VBox visitBox) {
        if(visit != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(patientSpecificVisitDetails));
                VBox visitView = loader.load();
                Label visitDate = (Label)visitView.lookup("#visitDate");
                //Label provider = (Label)visitView.lookup("#provider");
                Label issues = (Label)visitView.lookup("#issues");
                TextArea summary = (TextArea)visitView.lookup("#summary");
                TextArea evaluation = (TextArea)visitView.lookup("#evaluation");
                Label visitID = (Label)visitView.lookup("#visitID");

                visitDate.setText("Date: " + visit.date);
                //provider.setText("Provider seen: " + visit.doctorID); // take doctor & find the name
                issues.setText("Issues Addressed: " + visit.reason);
                summary.setText(visit.summary);
                evaluation.setText(visit.evaluation);
                visitID.setText(Long.toString(visit.id));
                visitBox.getChildren().addAll(visitView);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

