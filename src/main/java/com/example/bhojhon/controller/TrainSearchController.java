package com.example.bhojhon.controller;

import com.example.bhojhon.data.CartManager;
import com.example.bhojhon.data.DataManager;
import com.example.bhojhon.model.Train;
import com.example.bhojhon.util.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller for the Train Search screen.
 * Allows users to search for trains by train number.
 */
public class TrainSearchController extends BaseController {

    @FXML
    private TextField trainNumberField;

    @FXML
    private Label trainNameLabel;

    @FXML
    private Label routeLabel;

    @FXML
    private Label stationCountLabel;

    private Train selectedTrain;

    @Override
    public void initialize() {
        // Clear any previous selections
        selectedTrain = null;
        trainNameLabel.setText("");
        routeLabel.setText("");
        stationCountLabel.setText("");
    }

    /**
     * Handle search button click
     */
    @FXML
    private void handleSearch() {
        String trainNumber = trainNumberField.getText().trim();

        if (trainNumber.isEmpty()) {
            showAlert("Please enter a train number", Alert.AlertType.WARNING);
            return;
        }

        // Search for train
        selectedTrain = DataManager.getInstance().getTrainByNumber(trainNumber);

        if (selectedTrain == null) {
            showAlert("Train not found! Try: 101, 102, 201, or 202", Alert.AlertType.ERROR);
            trainNameLabel.setText("");
            routeLabel.setText("");
            stationCountLabel.setText("");
        } else {
            // Display train information
            trainNameLabel.setText(selectedTrain.getName());
            routeLabel.setText(selectedTrain.getRoute());
            stationCountLabel.setText(selectedTrain.getStations().size() + " stations");

            // Store train info in cart manager
            CartManager.getInstance().setSelectedTrainNumber(trainNumber);
        }
    }

    /**
     * Handle view stations button click
     */
    @FXML
    private void handleViewStations() {
        if (selectedTrain == null) {
            showAlert("Please search for a train first", Alert.AlertType.WARNING);
            return;
        }

        // Pass selected train to next screen
        navigationManager.putData("selectedTrain", selectedTrain);
        goToStationList();
    }

    /**
     * Handle back button
     */
    /**
     * Handle back button
     */
    @FXML
    private void handleBack() {
        goBack();
    }

    /**
     * Show alert dialog
     */
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Train Search");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
