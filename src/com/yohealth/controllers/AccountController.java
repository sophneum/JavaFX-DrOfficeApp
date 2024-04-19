package com.yohealth.controllers;

import com.yohealth.domain.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.net.URL;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AccountController extends BaseController {
    private String loginPage = "/com/resources/views/LoginPage.fxml";

    @FXML
    private TextField usernameTextField;

    public AccountController () {
        super();
    }

    // public due to being called from Main
    public void loadViewLoginPage() {
        try {
            URL fxmlUrl = getClass().getResource(loginPage);
            if (fxmlUrl == null) {
                throw new FileNotFoundException("LoginPage FXML file not found");
            }
            createStage(fxmlUrl);
            this.userPersistence.resetUserPersistence();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLoginButtonClicked() {
        String username = usernameTextField.getText();
        User user = this.userService.getUserByUsername(username);

        if (user != null) {
            this.userPersistence.setUserId(user.id);
            this.userPersistence.setUserName(user.username);
        } else {
            // invalid username message
            usernameTextField.clear();
            usernameTextField.promptTextProperty().setValue("Invalid username");
        }
    
        System.out.println("Hello, " + user.name + "!");
        if (user.roleID == 1) {
            PatientController patientController = new PatientController();
            patientController.loadViewPatientHomePage();
        } else if (user.roleID == 2) {
            NurseController nurseController = new NurseController();
            nurseController.loadViewNurseSearchPage();
        } else if (user.roleID == 3) {
            DoctorController doctorController = new DoctorController();
            doctorController.loadViewDoctorSearchPage();
        } else {
            // stay at login page
            System.out.println("Invalid role ID");
        }
    }
}