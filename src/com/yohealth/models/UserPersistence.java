package com.yohealth.models;

import javafx.stage.Stage;

public class UserPersistence {
    private String userName;
    private String previousPage;
    private long userId;
    private Stage mainStage;
    private long patientId;
    private long visitId;

    // Consider Scene persistence or a User object if needed.

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    public long getUserId() {
        return userId;
    }

    public long getVisitId() {
        return visitId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setVisitId(long visitId) {
       this.visitId = visitId;
    }

    public void resetUserPersistence() {
        userName = null;
        previousPage = null;
        userId = 0;
        patientId = 0;
        visitId = 0;
    }

    public void resetPatientID() {
        patientId = 0;
    }

    public void resetVisitPersistence() {
        visitId = 0;
    }

    public void setMainStage(Stage stage) {
        mainStage = stage;
    }
    
    public Stage getMainStage() {
        return mainStage;
    }
    public void setPatientID(long patientID) {
        this.patientId = patientID;
    }
    public long getPatientID(){
        return patientId;
    }

}