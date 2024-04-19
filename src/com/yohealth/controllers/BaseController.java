package com.yohealth.controllers;

import com.yohealth.Main;
import com.yohealth.models.UserPersistence;
import com.yohealth.repository.concrete.*;
import com.yohealth.services.UserPersistenceService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public abstract class BaseController extends Main {
    protected UserRepositoryDBService userService;
    protected RoleRepositoryDBService roleService;
    protected MessageRepositoryDBService messageService;
    protected VisitRepositoryDBService visitService;
    protected PrescriptionRepositoryDBService prescriptionService;
    protected UserPersistenceService userPersistenceService;
    protected UserPersistence userPersistence;


    public BaseController () {
        userService = RepositoryService.getUserService();
        roleService = RepositoryService.getRoleService();
        messageService = RepositoryService.getMessageService();
        visitService = RepositoryService.getVisitService();
        prescriptionService = RepositoryService.getPrescriptionService();
        userPersistenceService = UserPersistenceService.getInstance();
        userPersistence = userPersistenceService.getUserPersistence();
    }

    @FXML
    protected void onLogoutButtonClicked() {
        // Reset user persistence on logout.
        userPersistence.resetUserPersistence(); 
        // Load the login view
        AccountController controller = new AccountController();
        controller.loadViewLoginPage();
    }

    @FXML
    protected void onBackButtonClicked() {
        try {
            URL fxmlUrl = getClass().getResource(userPersistence.getPreviousPage());
            if (fxmlUrl == null) {
                throw new FileNotFoundException("Previous page FXML file not found");
            }
            createStage(fxmlUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void createStage(URL fxmlUrl) {
        try {
            if (fxmlUrl == null) {
                throw new FileNotFoundException("FXML file not found");
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            //System.out.println("FXML UR passed into createStage: " + fxmlUrl); // debug purposes
            Parent root = loader.load();
            Scene scene = new Scene(root);
            URL stylesheetURL = getClass().getResource("/com/resources/styles/yohealth.css");
            System.out.println("Stylesheet URL: " + stylesheetURL);
            scene.getStylesheets().add(stylesheetURL.toExternalForm());
            Stage primaryStage = userPersistence.getMainStage();
            primaryStage.setScene(scene);
            this.userPersistence.setMainStage(primaryStage);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
