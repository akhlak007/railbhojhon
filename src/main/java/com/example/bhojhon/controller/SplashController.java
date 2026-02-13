package com.example.bhojhon.controller;

import com.example.bhojhon.util.BaseController;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.util.Duration;

public class SplashController extends BaseController {

    @Override
    public void initialize() {
        // Auto-navigate to Main Menu after 3 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> navigateTo("/com/example/bhojhon/main-menu-view.fxml"));
        delay.play();
    }
}
