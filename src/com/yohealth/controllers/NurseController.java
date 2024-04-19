package com.yohealth.controllers;

import com.yohealth.domain.model.*;
import com.yohealth.utils.DateUtils;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableRow;

public class NurseController extends BaseController {
    private String nurseSearchPage = "/com/resources/views/NurseSearchPage.fxml";  
    private String nurseAddPatientPage = "/com/resources/views/NurseAddPatientPage.fxml";
    private String nursePatientDetails = "/com/resources/views/PatientDetailsNurseView.fxml";   

    private String visitsStyle = "/com/resources/views/visitsStyle.fxml";  
    private String messagesStyle  = "/com/resources/views/messagesStyle.fxml";      
    private String viewMessageStyle  = "/com/resources/views/viewMessageStyle.fxml"; 
    private String newMessageStyle  = "/com/resources/views/newMessageStyle.fxml"; 
    private String viewVisitStyle  = "/com/resources/views/viewVisitStyle.fxml"; 

    private User loadPatient; // used to store patient data
    private Collection<Visit> patientVisits; // used to store all patient visits
    private Collection<Message> patientMessages; // used to store all patient visits
    private Collection<Prescription> patientPrescriptions; // used to store patient prescriptions
    private Collection<User> allPatients; //  used to store all patient users

    public NurseController(){
        super();
    }
      // Patient Info TextField/Label/VBox/Button Declarations
    @FXML
    private TextField patientName,DOB,insuranceInfo, pharmacyInfo,mailingAddress, allergies ,medications, immunizations; 
      
    // Patient Vitals TextField/Label/HBox Declarations
    @FXML
    private TextField above12, weight, height, temperature,bloodPressureSystolic,bloodPressureDiastolic, enterText; 
    @FXML
    private Label patientPhoneNumber, emergencyContactNum, loadingDetails;
    @FXML
    private HBox emergencyContactBox, patientNumBox, searchBox;
    @FXML
    private Button add; 

    //Visits Label,Button, VBox declarations
    @FXML
    private Label visitDate,visitIssues,visitSummary,visitIDLabel,visitUpdateID,visitUpdateSuccess, loading;
    @FXML
    private HBox visitBoxButtons;
    @FXML
    private Button vBack,vEdit,vUpdate;
    @FXML
    private VBox visitVBox; // Box to add all visit VBoxes to before adding it to main VBox in visitStyle.fxml 
    @FXML 
    private TextField visitDateField,visitIssuesField, visitHeightField, visitWeightField,visitTempField, visitDiastolicField, visitSystolicField;
    @FXML
    private TextArea visitSummaryArea, visitEvaluationArea;

    // Messages Labels, Button, VBox declarations
    @FXML
    private Label messageDate,messageSubject, messageIDLabel, messageSuccess,loadingMessages;
    @FXML
    private Button onViewMessageButtonClicked, onBackToMessagesButtonClicked, onNewMessageButtonClicked, onSendMessagesButtonClicked, sendMessageButton, backToMeessagesButton;
    @FXML
    private HBox sendMessageButtons;
    @FXML
    private VBox messagesVBox, viewMessageVBox; // Box to add all messages VBoxes to before adding it to main VBox in visitStyle.fxml& viewMessageStyle.fxml
    @FXML 
    private TextArea messageBody;
    @FXML
    private TextArea messageContent;
    @FXML 
    private TextField messageSubjectField;

    // Nurse SP specific declarations
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> nameColumn, usrnColumn, dobColumn;

    @FXML // add new patient page fxml fields
    private TextField newPatientName, newPatientDOB, newPatientMailingAddress, newPatientBillingAddress, newPatientEmail, newPatientPhone, newPatientInsuranceInfo, newPatientPharmacyInfo, newPatientUsername;
    @FXML
    private Button addButton;



    @FXML
    private void initialize() {
        loadPatient = this.userService.getUserById(this.userPersistenceService.getUserPersistence().getPatientID());// Get the patient data
        patientVisits = this.visitService.getAllVisitsByPatientID(this.userPersistenceService.getUserPersistence().getPatientID()); // get patient visits)
        patientMessages = this.messageService.getAllMessagesByUserID(this.userPersistenceService.getUserPersistence().getPatientID());
        this.patientPrescriptions = this.prescriptionService.getAllPrescriptionsByPatientId(this.userPersistenceService.getUserPersistence().getPatientID());
        allPatients = this.userService.getUsersByRoleID(1); // used for search table
        patientPrescriptions = this.prescriptionService.getAllPrescriptionsByPatientId(this.userPersistenceService.getUserPersistence().getPatientID());
        
        
            
            //table.setItems(FXCollections.observableArrayList(allPatients));
            //mapToColumns();
        

        // check if any objects above failed to load patient data
        if(loadPatient == null)  {
            return;
        }
        
        if(patientPrescriptions == null) {
            // System.out.println("Failed to get patient prescriptions");
            // do not return here as some patients might not have prescriptions yet
        }
        
        if(patientVisits == null){
            System.out.println("Failed to get patient visits");
             // do not return here as some patients might not have messages or visits yet
        }

        if(patientMessages == null){
            System.out.println("Failed to get patient messages");
            // do not return here as some patients might not have messages or visits yet
        }

        if(allPatients == null) {System.out.println("Failed to load Patients");}

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

                medications.setText(Prescription.prescriptionsToNiceString(patientPrescriptions)); // this not displaying right now. the update user method and sql query need to have a section for prescriptions to be updated

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


            // Initialize and set other TextFields here
        });
    } 


    public void loadViewNurseSearchPage() {

        try {
            // table.getItems().clear();
            // Collection<User> allPatients = this.userService.getUsersByRoleID(1);
            // table.setItems(FXCollections.observableArrayList(allPatients));
            // mapToColumns();
            URL fxmlUrl = getClass().getResource(nurseSearchPage);
            if (fxmlUrl == null) {
                throw new FileNotFoundException("Nurse SP FXML file not found");
            }
            createStage(fxmlUrl);
            this.userPersistence.resetPatientID();
            this.userPersistence.setPreviousPage(nurseSearchPage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadViewNurseAddPatientPage() {
        try {
            URL fxmlUrl = getClass().getResource(nurseAddPatientPage);
            if (fxmlUrl == null) {
                throw new FileNotFoundException("Nurse AP FXML file not found");
            }
            createStage(fxmlUrl);
            this.userPersistence.setPreviousPage(nurseSearchPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadViewNursePatientDetails() {
        try {
            URL fxmlUrl = getClass().getResource(nursePatientDetails);
            if (fxmlUrl == null) {
                throw new FileNotFoundException("Nurse Patient Details FXML file not found");
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nursePatientDetails));
            Parent root = loader.load(); // load root node from view xml file into root instance of Parent object 

            NurseController nurseController = loader.getController();
            if(nurseController.patientVisits != null) {
                nurseController.loadPatientVisits(nurseController.patientVisits);
            }
            else {
                System.out.println("Patient visits are null");
            }

            if(nurseController.patientMessages != null) {
                nurseController.loadPatientMessages(nurseController.patientMessages, messagesVBox);
            }
            else {
                System.out.println("Patient messages are null");
            } 

            Scene scene = new Scene(root); // add root node to the new scene
            Stage primaryStage = userPersistence.getMainStage(); // use userPersistence instance to get primary stage
            primaryStage.setScene(scene); // set the scene of this primary stage to the scene object we instantiated above
            this.userPersistence.setMainStage(primaryStage); // use primary stage instance so set the main stage
            primaryStage.show();
            this.userPersistence.setPreviousPage(nurseSearchPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPatientVisits(Collection<Visit> patientVisits) throws IOException  { 
        HBox visitBoxTemplate = null;
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(visitsStyle));
        visitBoxTemplate = loader.load(); // load visit box template
        } catch (IOException e){
            e.printStackTrace();
        }

        if(visitBoxTemplate != null) {
            if(patientVisits.isEmpty()) { 
                Label inboxEmpty = new Label("Patient has no visits!");
                visitVBox.getChildren().add(inboxEmpty);
                return;
            }

            for( Visit visit : patientVisits) { // iterate over patient visits

                FXMLLoader loader = new FXMLLoader(getClass().getResource(visitsStyle));
                HBox visitBox = loader.load();

                // dynamic label declarations use fx:id for label we defined in visit style
                Label visitDate = (Label) visitBox.lookup("#visitDate");
                Label visitIssues = (Label) visitBox.lookup("#visitIssues");
                Label visitSummary = (Label) visitBox.lookup("#visitSummary");
                Label visitIDLabel = (Label) visitBox.lookup("#visitIDLabel");

                if( visitDate !=null && visit.date!= null) {
                    visitDate.setText(DateUtils.dateToPrettyString((visit.date)));
                }

                if(visitIssues!=null && visit.reason!=null){
                    visitIssues.setText(visit.reason);

                }

                if(visitSummary!=null && visit.summary!=null){
                    visitSummary.setText(visit.summary);
                }

                if(visitIDLabel!=null && visit.id!=0){
                    visitIDLabel.setText(Long.toString(visit.id));
                }
                
                    if(visitVBox != null){
                    visitVBox.getChildren().add(visitBox);
                }

            }
        } else {
            System.out.println("Failed to load visit box template");
        }
    }

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
        if(messagesVBox == null){
            messagesVBox = messagesBox;
        }
        if(messageBoxTemplate != null){
            if(patientMessages.isEmpty())
            { 
                Label inboxEmpty = new Label(" Patient Inbox Empty!");
                messagesVBox.getChildren().add(inboxEmpty);
                return;
            }
            for( Message message: patientMessages){ // iterate over patient visits

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
                if(messagesVBox != null)
                {
                    messagesVBox.getChildren().add(messageBox);
                }

            }
        } else {
            System.out.println("Failed to load messages box template");
        }
    } 

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

    private void loadViewNurseVisitDetails(Visit visit, VBox viewVisitVBox) throws IOException{ 
        HBox viewVisitBoxTemplate = null;
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewVisitStyle)); 
        viewVisitBoxTemplate = loader.load(); // load message box template
        }
        catch (IOException e){
            e.printStackTrace();  
        }
    
        if(viewVisitBoxTemplate != null){

            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewVisitStyle));
            HBox visitBox = loader.load();

            // dynamic label declarations use fx:id for label we defined in visit style
            TextField visitDateField = (TextField) visitBox.lookup("#visitDateField");
            TextField visitIssuesField = (TextField) visitBox.lookup("#visitIssuesField");
            TextArea visitSummaryArea = (TextArea) visitBox.lookup("#visitSummaryArea");
            TextArea visitEvaluationArea =(TextArea) visitBox.lookup("#visitEvaluationArea");
            TextField visitHeightField =(TextField) visitBox.lookup("#visitHeightField");
            TextField visitWeightField =(TextField) visitBox.lookup("#visitWeightField");
            TextField visitTempField =(TextField) visitBox.lookup("#visitTempField");
            TextField visitDiastolicField =(TextField) visitBox.lookup("#visitDiastolicField");
            TextField visitSystolicField =(TextField) visitBox.lookup("#visitSystolicField");
            Label visitUpdateID =(Label) visitBox.lookup("#visitUpdateID");
            
            if( visitDateField !=null && visit.date!= null) {
                    visitDateField.setText(DateUtils.dateToPrettyString((visit.date)));
                }

                if(visitIssuesField!=null && visit.reason!=null){
                    visitIssuesField.setText(visit.reason);
                }

                if(visitSummaryArea!=null && visit.summary!=null){
                    visitSummaryArea.setText(visit.summary);
                    visitSummaryArea.setWrapText(true);
                }

                if(visitEvaluationArea!=null && visit.evaluation!=null){
                    visitEvaluationArea.setText(visit.evaluation);
                    visitEvaluationArea.setWrapText(true);
                }
                
                if(visitHeightField!=null && visit.heightInCm!=0){
                    try{
                    visitHeightField.setText(Float.toString(visit.heightInCm));
                    }
                    catch(Exception e){
                        System.out.println("Failed to set height!");
                    }
                }
                
                if(visitWeightField!=null && visit.weightInKg!=0){
                    try{
                        visitWeightField.setText(Float.toString(visit.weightInKg));
                    }
                    catch(Exception e){
                        System.out.println("Failed to set weight field!");
                    }
                }

                if(visitTempField!=null && visit.temperatureInF!=0){
                    try{
                        visitTempField.setText(Float.toString(visit.temperatureInF));
                    }
                    catch(Exception e){
                        System.out.println("Failed to load temperature field!");
                    }
                }

                if(visitDiastolicField!=null && visit.bloodPressureDiastolic!=0){
                    try {
                        visitDiastolicField.setText((String.valueOf(visit.bloodPressureDiastolic)));
                    }
                    catch(Exception e){
                        System.out.println("Failed to set blood pressure diastolic!");
                    }
                }

                if(visitSystolicField!=null && visit.bloodPressureSystolic!=0){
                    try {
                        visitSystolicField.setText((String.valueOf(visit.bloodPressureSystolic)));
                    }
                    catch(Exception e){
                        System.out.println("Failed to set blood pressure systolic!");
                    }
                }

                if(visitUpdateID!=null && visit.id!=0){
                    visitUpdateID.setText((Long.toString(visit.id)));
                }

                if(viewVisitVBox != null)
                {
                        viewVisitVBox.getChildren().add(visitBox);
                }
        } else {
            System.out.println("Failed to load selected visit box template");
        }
    }

    @FXML 
    private void onSelectButtonClicked(ActionEvent event) throws IOException { // select visit button calls load visit details nurse view page
        Button button = (Button) event.getSource();

        // Get the parent container (HBox) of the button
        // keep getting parent containers until we reach main parent container holding all message box children

        VBox visitBox = (VBox) button.getParent();
        HBox parentContainer = (HBox)visitBox.getParent();
        VBox gparentContainer = (VBox) parentContainer.getParent();
        HBox ggParentContainer = (HBox) gparentContainer.getParent();
        VBox mainVisitBox = (VBox) ggParentContainer.getParent();
        mainVisitBox.getChildren().clear();

        long visitID =0;
        // Retrieve the message ID from the message box
        Label visitIDLabel = (Label)visitBox.lookup("#visitIDLabel");

        if(visitIDLabel != null)
        {
            visitID = Long.parseLong(visitIDLabel.getText());
        }

        Visit visit = this.visitService.getVisitByID(visitID);
        if(visit != null)
        {
            loadViewNurseVisitDetails(visit, mainVisitBox);
        }
    }

    @FXML
    private void onBackToVisitsButtonClicked(ActionEvent event){
       showLoadingVisitBox();
    }

    @FXML
    private void onUpdateVisitButtonClicked(ActionEvent event){
        Visit visit = new Visit();
        visit.id = Long.parseLong(visitUpdateID.getText());
        
        visit.date = new Date();

        visit.reason = visitIssuesField.getText();
        visit.summary = visitSummaryArea.getText();
        visit.evaluation = visitEvaluationArea.getText();
        visit.patientID = this.userPersistenceService.getUserPersistence().getPatientID();
        visit.doctorID = 0;

        try{
             visit.weightInKg = Float.parseFloat(visitWeightField.getText());
        }
        catch(NumberFormatException e){
            System.out.println("Weight must be a float value!");
        }
        catch(Exception e){
            System.out.println("Height must be a float value!");
        }

        try{
            visit.heightInCm = Float.parseFloat(visitHeightField.getText());
        }
        catch(NumberFormatException e){
            System.out.println("Height must be a float value!");
        }
        catch(Exception e){
            System.out.println("Height must be a float value!");
        }
        
        try{
            visit.temperatureInF = Float.parseFloat(visitTempField.getText());
        }
        catch(NumberFormatException e){
            System.out.println("Temperature must be a float value!");
        }
        catch(Exception e){
            System.out.println("Temperature must be a float value!");
        }
        try{
            visit.bloodPressureSystolic = Integer.parseInt(visitDiastolicField.getText());
        }
        catch(NumberFormatException e){
            System.out.println("BloodPressure Diastolic must be an integer value!");
        }
        catch(Exception e){
            System.out.println("BloodPressure Diastolic must be an integer value!");
        }

        try {
            visit.bloodPressureDiastolic = Integer.parseInt(visitSystolicField.getText());
        }
        catch(NumberFormatException e){
            System.out.println("BloodPressure Systolic must be an integer value!");
        }
        catch(Exception e){
            System.out.println("BloodPressure Systolic must be an integer value!");
        }

        this.visitService.updateVisit(visit);

        showLoadingVisitBox();
    }

    @FXML
    protected void onEditVisitButtonClicked(){
        visitEvaluationArea.setWrapText(false);
        visitSummaryArea.setWrapText(false);
        visitIssuesField.setEditable(true);
        visitSummaryArea.setEditable(true);
        visitEvaluationArea.setEditable(true);
        visitHeightField.setEditable(true);
        visitWeightField.setEditable(true);
        visitTempField.setEditable(true);
        visitDiastolicField.setEditable(true);
        visitSystolicField.setEditable(true);
    }

    @FXML
    private void onAddPatientButtonClicked() { // routes to UI where new patients are created
        loadViewNurseAddPatientPage();
    }
    
    @FXML
    private void onAddButtonClicked() { // adds the patient to the database
        try {
            User newPatient = new User();

            newPatient.name = newPatientName.getText();
            newPatient.dob = DateUtils.prettyStringToDate(newPatientDOB.getText());
            newPatient.mailingAddress = newPatientMailingAddress.getText();
            newPatient.billingAddress = newPatientBillingAddress.getText();
            newPatient.email = newPatientEmail.getText();
            newPatient.phone = newPatientPhone.getText();
            newPatient.insuranceInfo = newPatientInsuranceInfo.getText();
            newPatient.pharmacyInfo = newPatientPharmacyInfo.getText();
            newPatient.username = newPatientUsername.getText();
            newPatient.roleID = 1;

            this.userService.createUser(newPatient);

            System.out.println("New patient added successfully\n");

            // clear text fields
            newPatientName.clear();
            newPatientDOB.clear();
            newPatientMailingAddress.clear();
            newPatientBillingAddress.clear();
            newPatientEmail.clear();
            newPatientPhone.clear();
            newPatientInsuranceInfo.clear();
            newPatientPharmacyInfo.clear();
            newPatientUsername.clear();
        } catch (Exception e) {
            System.out.println("Failed to add new patient\n");
            e.printStackTrace();
        }
    }

    @FXML
    private void mapToColumns() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name));
        usrnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().username));
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
                    showLoadingDetails();
                }
            });
            return row;
        });
    }
    

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
        messagesVBox = newMessageBox;
    }

    @FXML 
    private void onNewMessageButtonClicked(ActionEvent event) throws IOException { 
        Button button = (Button) event.getSource();

        // Get the parent container (HBox) of the button
        // keep getting parent containers until we reach main parent container holding all message box children

        HBox messageBox = (HBox) button.getParent();
        VBox parentContainer = (VBox)messageBox.getParent();
        VBox ggParentContainer = (VBox) parentContainer.getParent();

        if (ggParentContainer.getChildren().size() >= 2) {
        Node secondChild = ggParentContainer.getChildren().get(1);

            if (secondChild instanceof ScrollPane) {
         // Get the content of the ScrollPane
                 ScrollPane scrollPane = (ScrollPane) secondChild;
                Node content = scrollPane.getContent();

                if (content instanceof AnchorPane) {
                // Access the AnchorPane directly
                    AnchorPane anchorPane = (AnchorPane) content;
                // Now you can work with the AnchorPane
                    VBox mainMessagesBox = (VBox) anchorPane.getChildren().get(0);
                    mainMessagesBox.getChildren().clear();
                    loadNewMessageView(mainMessagesBox);
                }
            }
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
    private void onEditVitalsButtonClicked() {
        above12.setEditable(true);
        weight.setEditable(true);
        height.setEditable(true);
        temperature.setEditable(true);
        bloodPressureSystolic.setEditable(true);
        bloodPressureDiastolic.setEditable(true);
    }

    @FXML
    private void onSaveVitalsButtonClicked() { // this instantiates a new visit object and saves visit details to visit table 
        above12.setEditable(false);
        weight.setEditable(false);
        height.setEditable(false);
        temperature.setEditable(false);
        bloodPressureSystolic.setEditable(false);
        bloodPressureDiastolic.setEditable(false);

        // call method to create visit with vitals information
        createVisit();
        loadViewNursePatientDetails(); // reload the view
    }

    @FXML
    private void onEditInfoButtonClicked() {
        DOB.setEditable(true);
        insuranceInfo.setEditable(true);
        pharmacyInfo.setEditable(true); // need to fix this text field
        allergies.setEditable(true);
        immunizations.setEditable(true);
        mailingAddress.setEditable(true);
        mailingAddress.setEditable(true);
    }

    @FXML
    private void onSaveInfoButtonClicked() {
        DOB.setEditable(false);
        insuranceInfo.setEditable(false);
        pharmacyInfo.setEditable(false); // need to fix this text field
        allergies.setEditable(false);
        medications.setEditable(false);
        immunizations.setEditable(false);
        mailingAddress.setEditable(false);
        // call Method to load patient info into SQL table
        updatePatientInfo();
    }
    @FXML
    private void onEmergencyCallButtonClicked(){
        patientNumBox.setVisible(true);
        emergencyContactBox.setVisible(true);
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


    private void updatePatientInfo(){
        try{
             String dobText = DOB.getText();
             if (dobText.matches("\\d{2}/\\d{2}/\\d{4}")) { // Check if the date string matches "DD/MM/YYYY" format
            loadPatient.dob = DateUtils.prettyStringToDate(dobText); // Convert string date back to date object
            } 
            else {
            // Date format is not as expected, handle it appropriately
            DOB.setText("DD/MM/YYYY");
            }
        }
        catch(Exception e){
            DOB.setText("DD/MM/YYYY");
        }

        loadPatient.insuranceInfo = insuranceInfo.getText();
        loadPatient.mailingAddress = mailingAddress.getText();
        loadPatient.pharmacyInfo = pharmacyInfo.getText();
        loadPatient.phone = patientPhoneNumber.getText();
        loadPatient.allergies = allergies.getText();
        loadPatient.immunizations = immunizations.getText();

        this.userService.updateUser(loadPatient); 
    }

    private void createVisit() { // creates a new visit object witht the data inside the patient vitals section

        Boolean isAbove12 = false;
        if(!above12.getText().equals("Y") && !above12.getText().equals("y")){ // if patient is not above 12 do not create a new visit object/ store patient vitals
            System.out.println("Patient must be above 12 years old to add vitals");
            isAbove12 = true;
        }
        
        Date currentDate = new Date(); // Get the current date for this visit
        System.out.println(currentDate);
        Visit newVisit = new Visit(); // create new visit object

        newVisit.patientID = this.userPersistenceService.getUserPersistence().getPatientID(); // set new visit patient ID to current patient ID
        newVisit.date = currentDate; // set new visit date
       
        if(height.getText() != null && isAbove12!= true) {
            try {
            newVisit.heightInCm = Float.parseFloat(height.getText()); // convert height string to float
            }
            catch (NumberFormatException e){
                System.out.println("Height must be an float value!");
            }
            catch (Exception e){
                System.out.println("Height must be an float value!");
            }
        }
        
        if(weight.getText() != null && isAbove12!= true) {
            try{
            newVisit.weightInKg = Float.parseFloat(weight.getText()); // convert weight string to float
            }
            catch(NumberFormatException e){
                System.out.println("Weight must be an float value!");
            }
            catch(Exception e){
                System.out.println("Weight must be an float value!");
            }
        }
        
        if(temperature.getText() != null && isAbove12!= true) {
            try {
            newVisit.temperatureInF = Float.parseFloat(temperature.getText()); // convert height string to float
            }
            catch (NumberFormatException e){
                System.out.println("Temperature must be an float value!");
            }
            catch (Exception e){
                System.out.println("Weight must be an float value!");
            }
        }
        
        if(bloodPressureDiastolic.getText() != null && isAbove12!= true) {
            try {
            newVisit.bloodPressureDiastolic = Integer.parseInt(bloodPressureDiastolic.getText()); // convert diastolic pressure string to int
            }
            catch (NumberFormatException e) {
                System.out.println("Blood Pressure Diastolic must be an integer value!");
            }
            catch (Exception e) {
                System.out.println("Blood Pressure Diastolic must be an integer value!");
            }
        }

        if(bloodPressureSystolic.getText() != null && isAbove12!= true) {
            try{
            newVisit.bloodPressureSystolic= Integer.parseInt(bloodPressureSystolic.getText()); // convert systolic pressure string to int
            }
            catch (NumberFormatException e) {
                System.out.println("Blood Pressure Diastolic must be an integer value!");
            }
            catch (Exception e) {
                System.out.println("Blood Pressure Diastolic must be an integer value!");
            }
        }

        this.visitService.createVisit(newVisit); // load new visit into database
    }

    private void showLoadingVisitBox() {
        visitBoxButtons.getChildren().remove(vBack);
        visitBoxButtons.getChildren().remove(vEdit);
        visitBoxButtons.getChildren().remove(vUpdate);

        loading.setVisible(true);

        // Show loading message

        // Delay the execution of loadViewNursePatientDetails by 1 second
        PauseTransition pause = new PauseTransition();
        pause.setDuration(javafx.util.Duration.seconds(1));
        pause.setOnFinished(e -> loadViewNursePatientDetails());
        pause.play();
    }

    private void showLoadingMessagesBox() {
        sendMessageButtons.getChildren().remove(sendMessageButton);

        loadingMessages.setVisible(true);

        // Show loading message

        // Delay the execution of loadViewNursePatientDetails by 1 second
        PauseTransition pause = new PauseTransition();
        pause.setDuration(javafx.util.Duration.seconds(1));
        pause.setOnFinished(e -> loadViewNursePatientDetails());
        pause.play();
    }

    private void showLoadingDetails() {
        searchBox.getChildren().remove(add);

        loadingDetails.setVisible(true);

        // Show loading message

        // Delay the execution of loadViewNursePatientDetails by 1 second
        PauseTransition pause = new PauseTransition();
        pause.setDuration(javafx.util.Duration.seconds(1));
        pause.setOnFinished(e -> loadViewNursePatientDetails());
        pause.play();
    }
}
