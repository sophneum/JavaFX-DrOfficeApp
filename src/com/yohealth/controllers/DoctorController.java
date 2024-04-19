package com.yohealth.controllers;

import com.yohealth.domain.model.*;
import com.yohealth.utils.DateUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import java.util.Collection;
import java.util.Date;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableRow;
import javafx.collections.FXCollections;
import javafx.scene.input.MouseButton;
import javafx.beans.property.SimpleStringProperty;

public class DoctorController extends BaseController {
    private String doctorSearchPage = "/com/resources/views/DoctorSearchPage.fxml";
    private String doctorPatientDetails = "/com/resources/views/DoctorPatientDetails.fxml";
    private String doctorVisitDetails = "/com/resources/views/DoctorVisitDetails.fxml";

    private User loadPatient; // used to store patient data
    private Collection<Visit> patientVisits; // used to store all patient visits
    private Collection<Message> patientMessages; // used to store all patient visits
    private Collection<Prescription> patientPrescriptions; // used to store patient prescriptions
    private Collection<User> allPatients; //  used to store all patient users
    private Visit patientVisit;

    private String visitsDoctorStyle = "/com/resources/views/visitsDoctorStyle.fxml";
    private String viewMessageStyle  = "/com/resources/views/viewMessageStyle.fxml";
    private String newMessageStyle  = "/com/resources/views/doctorNewMessageStyle.fxml";
    private String messagesStyle  = "/com/resources/views/doctorMessagesStyle.fxml";

    private String doctorViewVisit = "/com/resources/views/DoctorViewVisit.fxml";

    public DoctorController () {
        super();
    }

    @FXML
    private TextField patientName, DOB, insuranceInfo, pharmacyInfo, mailingAddress, allergies ,medications, immunizations, messageSubjectField, summaryField, reasonField, dateField,evaluationField;
    @FXML
    private TextArea messageBody;
    @FXML
    private Button searchButt, newMessButt, emergencyButt, backButt, logoutButt, selecButton, sendMessageButton;
    @FXML
    private AnchorPane visitAnchorPane;
    @FXML
    private VBox visitVBox, messageVBox;
    @FXML
    private HBox emergencyContactBox, patientNumBox, sendMessageButtons;
    @FXML
    private Label patientPhoneNumber, loadingMessages, emergencyContactNum, visitEvaluation, messageSuccess, loadingDeets;
    @FXML
    private boolean hasNoPrescrips;
    @FXML 
    private long realID;
    @FXML
    private User currPatient;
    @FXML
    private TextArea visitSummary;
    @FXML
    private Label prescriptionLabel, dosageLabel, startDateLabel, pharmacyLabel, visitLabelSummary; // used by kiernan
    @FXML
    private VBox prescriptionBox, dosageBox, startDateBox, pharmacyBox;   
    @FXML
    private Button editVisit, saveVisit, orderButton;
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> nameColumn, usrnColumn, dobColumn;
    @FXML
    private TextField enterText; // search patient field

    // used for order button
    private String prescriptionName, startDate, dosage; 
    @FXML
    private VBox prescriptionConfirmationBox;
    @FXML
    private Label prescriptionNameConfirmation, prescriptionDosageConfirmation, prescriptionStartDateConfirmation, pharmacyConfirmation, allFieldsRequiredLabel, dateFormatRequiredLabel;
    @FXML
    private TextField prescriptionNameTextField, dosageTextField, startDateTextField;

    @FXML
    private VBox bigBox, doctorContainer;
    @FXML
    private HBox stupidHBox;
    @FXML
    private VBox stupidVBox;
    @FXML
    private ScrollPane doctorScrollPane;
    @FXML
    private Button savevisitButton, editvisitButton;
    @FXML
    private Label reason, summary, visitViewEvaluation, visitDate, visitDateText;
    @FXML
    private TextArea reasonTextField, summaryTextField, visitViewEvaluationTextField;

    private String specificVisitDate;

    @FXML
    private void initialize() {

        loadPatient = this.userService.getUserById(this.userPersistenceService.getUserPersistence().getPatientID());// Get the patient data
        patientVisits = this.visitService.getAllVisitsByPatientID(this.userPersistenceService.getUserPersistence().getPatientID()); // get patient visits
        patientMessages = this.messageService.getAllMessagesByUserID(this.userPersistenceService.getUserPersistence().getPatientID()); // get patient messages
        patientPrescriptions = this.prescriptionService.getAllPrescriptionsByPatientId(this.userPersistenceService.getUserPersistence().getPatientID()); // get patients prescriptions
        allPatients = this.userService.getUsersByRoleID(1); // used for search table
        patientVisit = this.visitService.getVisitByID(this.userPersistenceService.getUserPersistence().getVisitId());
        
        if(this.userPersistence.getVisitId() != 0) {
            specificVisitDate = DateUtils.dateToPrettyString(this.visitService.getVisitByID(this.userPersistence.getVisitId()).date);
        }

        // check if any objects above failed to load patient data
        if (loadPatient == null)  {
            return;
        }
        
        hasNoPrescrips = false;
        if (patientPrescriptions == null && hasNoPrescrips == false) {
            System.out.println("Patient has zero prescriptions");
            hasNoPrescrips = true;
            // do not return here as some patients might not have prescriptions yet
        }
        
        if (patientVisits == null) {
            System.out.println("Patient has no visits");
             // do not return here as some patients might not have messages or visits yet
        }

        if (patientMessages == null) {
            System.out.println("Patient has no messages");
            // do not return here as some patients might not have messages or visits yet
        }



            
        Platform.runLater(() -> {
     
            if (patientName != null && loadPatient.name != null) {
                patientName.setText(loadPatient.name);
            }
            if (DOB != null && loadPatient.dob != null) {
                DOB.setText(DateUtils.dateToPrettyString(loadPatient.dob));
            }
            if (insuranceInfo != null && loadPatient.insuranceInfo != null) {
                insuranceInfo.setText(loadPatient.insuranceInfo);
            }
            if (pharmacyInfo != null && loadPatient.pharmacyInfo != null) {
                pharmacyInfo.setText(loadPatient.pharmacyInfo);
            }
            if (mailingAddress != null && loadPatient.mailingAddress != null) {
                mailingAddress.setText(loadPatient.mailingAddress);
            }
            if (allergies != null && loadPatient.allergies != null) {
                allergies.setText(loadPatient.allergies);
            }
            if (medications != null && patientPrescriptions != null) {
                medications.setText(Prescription.prescriptionsToNiceString(patientPrescriptions));
            }
            if (immunizations != null && loadPatient.immunizations != null) {
                immunizations.setText(loadPatient.immunizations);
            }
            if (patientPhoneNumber != null && loadPatient.phone != null) {
                patientPhoneNumber.setText(loadPatient.phone);
            }
            if (emergencyContactNum != null && loadPatient.emergencyContactPhone!= null) {
                emergencyContactNum.setText(loadPatient.emergencyContactPhone);
            }
            if (visitDateText != null && patientVisit.date != null) {
                visitDateText.setText(specificVisitDate);
            }
            if (reasonTextField!= null && patientVisit.reason != null) {
                reasonTextField.setText(patientVisit.reason);
            }
            if (summaryTextField!= null && patientVisit.summary != null) {
                summaryTextField.setText(patientVisit.summary);
            }
            if (visitViewEvaluationTextField!= null && patientVisit.evaluation!= null) {
                visitViewEvaluationTextField.setText(patientVisit.evaluation);
            }
        });
    }

    public void loadViewDoctorSearchPage() {
        try {
            URL fxmlUrl = getClass().getResource(doctorSearchPage);
            if (fxmlUrl == null) {
                throw new FileNotFoundException("Doctor search FXML file not found");
            }
            createStage(fxmlUrl);
            this.userPersistence.resetPatientID();
            this.userPersistence.setPreviousPage(doctorSearchPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadViewDoctorPatientDetails() {
        try {
            //this.userPersistence.resetVisitPersistence();
            this.userPersistence.setPreviousPage(doctorSearchPage);
            this.userPersistence.resetVisitPersistence();
            URL fxmlUrl = getClass().getResource(doctorPatientDetails);
            if (fxmlUrl == null) {
                throw new FileNotFoundException("Doctor Patient Details FXML file not found");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(doctorPatientDetails));
            Parent root = loader.load(); // load root node from view xml file into root instance of Parent object 

            DoctorController doctorController = loader.getController();
            if(doctorController.patientVisits != null) {
                doctorController.loadPatientVisits(doctorController.patientVisits);
            }
            else {
                System.out.println("Patient visits are null");
            }

            if(doctorController.patientMessages != null) {
                doctorController.loadPatientMessages(doctorController.patientMessages, messageVBox);
            }
            else {
                System.out.println("Patient messages are null");
            } 

            Scene scene = new Scene(root); // add root node to the new scene
            Stage primaryStage = userPersistence.getMainStage(); // use userPersistence instance to get primary stage
            primaryStage.setScene(scene); // set the scene of this primary stage to the scene object we instantiated above
            this.userPersistence.setMainStage(primaryStage); // use primary stage instance so set the main stage
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassCastException ce) {
            ce.printStackTrace();
        }
    }

    @FXML
    private void loadPatientMessages(Collection<Message> patientMessages, VBox messagesBox) throws IOException  {
        // load patient messages
        HBox messageBoxTemplate = null;
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(messagesStyle));
        messageBoxTemplate = loader.load(); // load message box template
        }
        catch (IOException e){
            e.printStackTrace();
        }
        if (messageVBox == null){
            messageVBox = messagesBox;
        }
        if (messageBoxTemplate != null){

            for (Message message: patientMessages){ // iterate over patient visits

                FXMLLoader loader = new FXMLLoader(getClass().getResource(messagesStyle));
                HBox messageBox = loader.load();

                // dynamic label declarations use fx:id for label we defined in visit style
                Label messageDate = (Label) messageBox.lookup("#messageDate");
                Label messageSubject = (Label) messageBox.lookup("#messageSubject");
                Label messageIDLabel = (Label) messageBox.lookup("#messageIDLabel");
                
                if( messageDate !=null && message.date!= null) {
                messageDate.setText(DateUtils.dateToPrettyString((message.date)));
                }

                if(messageSubject!=null && message.subject!=null){
                    messageSubject.setText(message.subject);
                }

                if(messageIDLabel!=null && message.id!=0){
                    messageIDLabel.setText(Long.toString(message.id));
                }
                if(messageVBox != null)
                {
                    messageVBox.getChildren().add(messageBox);
                }
            }
        } 
        else {
            System.out.println("Failed to load messages box template");
        }
    }

    @FXML
    private void loadPatientVisits(Collection<Visit> patientVisits) throws IOException  {
        HBox visitBoxTemplate = null;
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(visitsDoctorStyle));
        visitBoxTemplate = loader.load(); // load visit box template holding visitsDoctorStyle
        }
        catch (IOException e){
            e.printStackTrace();
        }

        if (visitBoxTemplate != null) {

            for (Visit visit : patientVisits) { // iterate over patient visits

                FXMLLoader loader = new FXMLLoader(getClass().getResource(visitsDoctorStyle));
                HBox visitBox = loader.load();

                // dynamic label declarations use fx:id for label we defined in visit style
                Label visitDate = (Label) visitBox.lookup("#visitDate");
                Label visitIssues = (Label) visitBox.lookup("#visitIssues");
                Label visitLabelSummary = (Label) visitBox.lookup("#visitLabelSummary");
                Label visitIDLabel = (Label) visitBox.lookup("#visitIDLabel");

                Button selectButton = (Button) visitBox.lookup("#selectButton");
                selectButton.setUserData(visit.id);

                
                if( visitDate !=null && visit.date!= null) {
                    visitDate.setText(DateUtils.dateToPrettyString((visit.date)));
                }

                if(visitIssues!=null && visit.reason!=null){
                    visitIssues.setText(visit.reason);
                }

                if(visitLabelSummary!=null && visit.summary!=null){
                    visitLabelSummary.setText(visit.summary);
                }

                if(visitIDLabel!=null && visit.id!=0){
                    visitIDLabel.setText(Long.toString(visit.id));
                }
                
                if(visitVBox != null){
                    visitVBox.getChildren().add(visitBox);
                }
            }
        }
        else {
            System.out.println("Failed to load visit box template");
        }
    }

    @FXML
    private void onEmergencyCallButtonClicked(){
        patientNumBox.setVisible(true);
        emergencyContactBox.setVisible(true);
    }

    @FXML
    private void onSelectButtonClicked(ActionEvent event) throws IOException { // select visit button calls load visit details nurse view page
        Button thisButton = (Button) event.getSource();
        realID = (long) thisButton.getUserData();
        this.userPersistence.setVisitId(realID);
        loadViewDoctorVisitDetails();
        
    }

    @FXML
    public void loadViewDoctorVisitDetails() {
        try{
            this.userPersistence.setPreviousPage(doctorPatientDetails);
            URL fxmUrl = getClass().getResource(doctorVisitDetails);
            if(fxmUrl == null) {
                throw new FileNotFoundException("DoctorVisitDetails FXM file not found");
            }
            createStage(fxmUrl);
            populateVisit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateVisit() {
        long visitID = this.userPersistence.getVisitId();
        Visit visit = visitService.getVisitByID(visitID);
        
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(doctorViewVisit));
        VBox visitBox = loader.load();

        
        TextArea summaryField = (TextArea)visitBox.lookup("#summaryTextField");
        TextArea reasonField = (TextArea)visitBox.lookup("#reasonTextField");
       // Label dateField = (Label)visitBox.lookup("#visitDateText");
        TextArea visitViewEvaluationTextField = (TextArea)visitBox.lookup("#visitViewEvaluationTextField");
        
        if(visit.summary!=null){
            summaryField.setText(visit.summary);
        }

        if(visit.reason!=null){
            reasonField.setText(visit.reason);
        }
       // dateField.setText(DateUtils.dateToPrettyString(visit.date));

        if(visit.evaluation != null){
            visitViewEvaluationTextField.setText(visit.evaluation);
        }

        }
        catch (IOException e) {
            System.out.println("Error loading fxml visit file");
        }
        catch (Exception e) {
            System.out.println("Failed to load patient visit details");
        }
    }

    @FXML
    private void onOrderButtonClicked() {
        try {
            // read in text
            prescriptionName = prescriptionNameTextField.getText();
            dosage = dosageTextField.getText();
            startDate = startDateTextField.getText();

            // throw error if all fields not filled
            if ((prescriptionName.isEmpty()) || (dosage.isEmpty()) || (startDate.isEmpty())) {
                System.out.println("Prescription ordering text fields are empty.");
                allFieldsRequiredLabel.setVisible(true);
                dateFormatRequiredLabel.setVisible(false);
                prescriptionConfirmationBox.setVisible(false);
            } else {
                // all filled text fields, hide pop-up error
                allFieldsRequiredLabel.setVisible(false);
                dateFormatRequiredLabel.setVisible(false);

                // catch date format exception
                try {
                    String dateRegex = "^(0[1-9]|1[0-2])\\/(0[1-9]|[12]\\d|3[01])\\/(19|20)\\d{2}$";
                    String startDate = startDateTextField.getText();

                    if (startDate.matches(dateRegex)) {
                        // create a prescription to save to the database
                        Prescription newPrescription = new Prescription();
                        newPrescription.patientID = this.userPersistence.getPatientID();
                        newPrescription.name = prescriptionName;
                        newPrescription.dose = dosage;
                        newPrescription.startDate = DateUtils.prettyStringToDate(startDate);

                        // save prescription to database
                        this.prescriptionService.createPrescription(newPrescription);

                        // pop up with prescription confirmation details
                        prescriptionNameConfirmation.setText(prescriptionName);
                        prescriptionDosageConfirmation.setText(dosage);    
                        prescriptionStartDateConfirmation.setText(startDate);
                        pharmacyConfirmation.setText(this.userService.getUserById(this.userPersistence.getPatientID()).pharmacyInfo);      
                        prescriptionConfirmationBox.setVisible(true);      

                        // clear text fields
                        prescriptionNameTextField.clear();
                        dosageTextField.clear();
                        startDateTextField.clear();
                    } else {
                        System.out.println("Date format exception - Prescription not saved to database");
                        dateFormatRequiredLabel.setVisible(true);
                        //throw new ParseException("Date format exception", 0);
                    }
                } catch (Exception e) {
                    // print the error message, not the stack trace
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mapToColumns() {

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name));
        usrnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().username));
        // dobColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().dob.toString()));
        dobColumn.setCellValueFactory(cellData -> {
            String dobString = cellData.getValue().dob != null ? cellData.getValue().dob.toString() : "N/A";
            return new SimpleStringProperty(dobString);
        });
    }


    @FXML
    protected void onSearchButtonClicked() {
        
        table.getItems().clear(); //clear old items precaution
        String patientName = "%" + enterText.getText() + "%";
        Collection<User> allPatsWithName = this.userService.getUsersByNameAndRoleID(patientName, 1);

        table.setItems(FXCollections.observableArrayList(allPatsWithName));

        mapToColumns();
        
        table.setRowFactory(tv -> {

            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                if ((!row.isEmpty()) && (event.getButton() == MouseButton.PRIMARY) && (event.getClickCount() == 1)) {

                    User patClicked = row.getItem();
                    long currPat = patClicked.id;
                    this.userPersistence.setPatientID(currPat);
                    System.out.println(currPat);
                    showLoadingDetails();
                }
            });
            return row;
        });

    }

    @FXML
    protected void onsaveVisitButtonClicked()  {
        //Button button = (Button)event.getSource();

        long visitID = this.userPersistence.getVisitId();
        Visit visit = visitService.getVisitByID(visitID);

        reasonTextField.setEditable(false);
        summaryTextField.setEditable(false);
        visitViewEvaluationTextField.setEditable(false);

        visit.reason = reasonTextField.getText();
        visit.summary =  summaryTextField.getText();
        visit.evaluation = visitViewEvaluationTextField.getText();
        visitService.updateVisit(visit);
        // repopulate the summary to show new information
    }

    @FXML
    protected void oneditVisitButtonClicked() {
        reasonTextField.setEditable(true);
        summaryTextField.setEditable(true);
        visitViewEvaluationTextField.setEditable(true);
    }


    @FXML
    protected void onBackButtonClickedFromDrVisitDetails() {
        loadViewDoctorPatientDetails();
    }

    @FXML
    private void loadNewMessageView(VBox newMessageBox) throws IOException { 

        HBox newMessageBoxTemplate = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(newMessageStyle)); 
            newMessageBoxTemplate = loader.load(); // load message box template
        } catch (IOException e) {
            e.printStackTrace();  
        }
        if (newMessageBoxTemplate != null) {
            newMessageBox.getChildren().add(newMessageBoxTemplate);
        } else {
            System.out.println("Failed to get new message box template");
        }

        messageVBox = newMessageBox;
    }

    @FXML
    private void onNewMessageButtonClicked(ActionEvent e) throws IOException {

        Button button = (Button) e.getSource();
        HBox messageBox = (HBox) button.getParent();
        VBox parentContainer = (VBox)messageBox.getParent();
        VBox ggParentContainer = (VBox) parentContainer.getParent();

        if (ggParentContainer.getChildren().size() >= 2) {
        Node secondChild = ggParentContainer.getChildren().get(1);

            if (secondChild instanceof ScrollPane) {
        
                ScrollPane scrollPane = (ScrollPane) secondChild;
                Node content = scrollPane.getContent();

                if (content instanceof AnchorPane) {
                
                    AnchorPane anchorPane = (AnchorPane) content;
                    VBox mainMessagesBox = (VBox) anchorPane.getChildren().get(0);
                    mainMessagesBox.getChildren().clear();

                    loadNewMessageView(mainMessagesBox);
                }
            }
        }
    }

    @FXML
    protected void onViewMessageButtonClicked(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();

        // Get the parent container (HBox) of the button
        // keep getting parent containers until we reach main parent container holding all message box children

        VBox messageBox = (VBox) button.getParent();
        HBox parentContainer = (HBox)messageBox.getParent();
        VBox gparentContainer = (VBox) parentContainer.getParent();
        HBox ggParentContain = (HBox) gparentContainer.getParent();
        VBox mainMessagesBox = (VBox) ggParentContain.getParent();
        mainMessagesBox.getChildren().clear();

        long messageID =0;
        // Retrieve the message ID from the message box
        Label messageIDLabel = (Label) messageBox.lookup("#messageIDLabel");

        if(messageIDLabel != null) {
            messageID = Long.parseLong(messageIDLabel.getText());
        }

        Message message = this.messageService.getMessageByID(messageID);
        if(message != null) {
            viewMessage(message,mainMessagesBox);
        }
    }

    @FXML
    private void onBackToMessagesButtonClicked(ActionEvent event) throws IOException{
        Button button = (Button) event.getSource();

        // Get the parent container (HBox) of the button
        // keep getting parent containers until we reach main parent container holding all message box children

        VBox messageBox = (VBox) button.getParent();
        HBox parentContainer = (HBox)messageBox.getParent();
        VBox gparentContainer = (VBox) parentContainer.getParent();
        HBox ggParentContain = (HBox) gparentContainer.getParent();
        VBox mainMessagesBox = (VBox) ggParentContain.getParent();
        mainMessagesBox.getChildren().clear();

        loadPatientMessages(patientMessages, mainMessagesBox);
    }

    @FXML
    private void viewMessage(Message viewMessage, VBox viewMessageVBox) throws IOException  { 
        // load patient messages
        HBox viewMessageBoxTemplate = null;
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewMessageStyle)); 
        viewMessageBoxTemplate = loader.load(); // load message box template
        } catch (IOException e){
            e.printStackTrace();  
        }

        if(viewMessageBoxTemplate != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewMessageStyle));
            HBox messageBox = loader.load();

            // dynamic label declarations use fx:id for label we defined in visit style
            Label messageDate = (Label) messageBox.lookup("#messageDate");
            Label messageSubject = (Label) messageBox.lookup("#messageSubject");
            TextArea messageContent = (TextArea) messageBox.lookup("#messageContent");
            
            if( messageDate !=null && viewMessage.date!= null) {
                messageDate.setText(DateUtils.dateToPrettyString((viewMessage.date)));
            }

            if(messageSubject!=null && viewMessage.subject!=null){
                messageSubject.setText(viewMessage.subject);
            }

            if(messageContent!=null && viewMessage.content!=null){
                messageContent.setText(viewMessage.content);
            }
            if(viewMessageVBox != null)
            {
                viewMessageVBox.getChildren().add(messageBox);
            }
        } else {
            System.out.println("Failed to view message box template");
        }
    }

    @FXML
   private void onSendMessagesButtonClicked(){
    
        String sumtin = messageBody.getText();
        String sumthin = messageSubjectField.getText();
        if(sumtin.isEmpty() || sumthin.isEmpty() || sumtin == null || sumthin == null){
            messageBody.setText("Must enter a valid body message");
            messageSubjectField.setText("Must enter a valid subject");
            return;
        }

        Message message = new Message();
        message.patientID = (this.userPersistenceService.getUserPersistence().getPatientID());
        message.subject = messageSubjectField.getText();
        message.date = new Date();
        message.content = messageBody.getText();
        int sent = this.messageService.createMessage(message);
        if( sent == 1){
            messageSuccess.setVisible(true);
        }
        showLoadingMessagesBox();
   }

    @FXML
    private void showLoadingMessagesBox() {
        sendMessageButtons.getChildren().remove(sendMessageButton);

        loadingMessages.setVisible(true);

        // Show loading message

        // Delay the execution of loadViewNursePatientDetails by 1 second
        PauseTransition pause = new PauseTransition();
        pause.setDuration(javafx.util.Duration.seconds(1));
        pause.setOnFinished(e -> loadViewDoctorPatientDetails());
        pause.play();
    }

    @FXML
    private void showLoadingDetails() {

        loadingDeets.setVisible(true);

        // Show loading message

        // Delay the execution of loadViewNursePatientDetails by 1 second
        PauseTransition pause = new PauseTransition();
        pause.setDuration(javafx.util.Duration.seconds(1));
        pause.setOnFinished(e -> loadViewDoctorPatientDetails());
        pause.play();
    }
}