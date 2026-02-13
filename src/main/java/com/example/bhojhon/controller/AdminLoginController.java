package com.example.bhojhon.controller;

import com.example.bhojhon.util.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminLoginController extends BaseController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize() {
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String pass = passwordField.getText();

        if (email.equals("admin@gmail.com") && pass.equals("admin@gmail.com")) {
            navigateTo("/com/example/bhojhon/admin-dashboard-view.fxml", "Admin Dashboard");
        } else {
            errorLabel.setText("Invalid credentials.");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void handleBack() {
        goBack();
    }
}
