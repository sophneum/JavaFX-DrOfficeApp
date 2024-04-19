package com.yohealth;

import com.yohealth.repository.concrete.*;
import com.yohealth.services.UserPersistenceService;
import com.yohealth.controllers.*;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.text.Font;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // load font families into application
        Font.getFamilies();

        // initialize services
        UserPersistenceService.initialize();
        RepositoryService.initialize();
        UserPersistenceService userPersistenceService = UserPersistenceService.getInstance();

        // set main stage
        AccountController controller = new AccountController();
        userPersistenceService = UserPersistenceService.getInstance();
        userPersistenceService.getUserPersistence().setMainStage(primaryStage);
        userPersistenceService.getUserPersistence().setUserId(1);
        controller.loadViewLoginPage();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
