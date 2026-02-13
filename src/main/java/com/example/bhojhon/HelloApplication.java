package com.example.bhojhon;

import com.example.bhojhon.util.NavigationManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Bootstrap the app using the central NavigationManager so every screen
        // is styled and navigation works consistently.
        NavigationManager navigationManager = NavigationManager.getInstance();
        navigationManager.setPrimaryStage(stage);
        navigationManager.navigateTo(
                "/com/example/bhojhon/splash-view.fxml",
                "Upload Ticket - Train Food Pre-Order");
        stage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch();
    }
}