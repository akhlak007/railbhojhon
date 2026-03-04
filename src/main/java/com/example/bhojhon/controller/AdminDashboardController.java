package com.example.bhojhon.controller;

import com.example.bhojhon.data.DatabaseHelper;
import com.example.bhojhon.model.OrderInfo;
import com.example.bhojhon.model.RestaurantOwner;
import com.example.bhojhon.util.BaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AdminDashboardController extends BaseController {

    // ===== Stat Cards =====
    @FXML
    private Label statRestaurantsValue;
    @FXML
    private Label statOrdersValue;
    @FXML
    private Label statStationsValue;

    // ===== Orders Tab =====
    @FXML
    private TableView<OrderInfo> ordersTable;
    @FXML
    private TableColumn<OrderInfo, String> orderIdCol;
    @FXML
    private TableColumn<OrderInfo, String> nameCol;
    @FXML
    private TableColumn<OrderInfo, String> phoneCol;
    @FXML
    private TableColumn<OrderInfo, String> trainCol;
    @FXML
    private TableColumn<OrderInfo, String> seatCol;
    @FXML
    private TableColumn<OrderInfo, String> stationCol;
    @FXML
    private TableColumn<OrderInfo, String> itemsCol;
    @FXML
    private TableColumn<OrderInfo, Double> totalCol;
    @FXML
    private TableColumn<OrderInfo, String> dateCol;

    // ===== Restaurants Tab =====
    @FXML
    private TableView<RestaurantOwner> restaurantsTable;
    @FXML
    private TableColumn<RestaurantOwner, String> restaurantNameCol;
    @FXML
    private TableColumn<RestaurantOwner, String> ownerEmailCol;
    @FXML
    private TableColumn<RestaurantOwner, String> restaurantStationCol;
    @FXML
    private TableColumn<RestaurantOwner, String> regDateCol;
    @FXML
    private Label totalRestaurantsLabel;

    @Override
    public void initialize() {
        // Wire Orders columns
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("passengerName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        trainCol.setCellValueFactory(new PropertyValueFactory<>("trainNumber"));
        seatCol.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        stationCol.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        itemsCol.setCellValueFactory(new PropertyValueFactory<>("orderItems"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        // Wire Restaurants columns
        restaurantNameCol.setCellValueFactory(new PropertyValueFactory<>("restaurantName"));
        ownerEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        restaurantStationCol.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        regDateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        refreshAll();
    }

    /** Called by the Refresh button in the header */
    @FXML
    private void loadData() {
        refreshAll();
    }

    /** Refresh everything: stat cards + both tables */
    private void refreshAll() {
        DatabaseHelper db = new DatabaseHelper();

        // Stat cards
        if (statRestaurantsValue != null)
            statRestaurantsValue.setText(String.valueOf(db.getRestaurantCount()));
        if (statOrdersValue != null)
            statOrdersValue.setText(String.valueOf(db.getOrderCount()));
        if (statStationsValue != null)
            statStationsValue.setText(String.valueOf(db.getStationCount()));

        // Orders table
        ordersTable.setItems(FXCollections.observableArrayList(db.getAllOrdersInfo()));

        // Restaurants table
        loadRestaurants(db);
    }

    @FXML
    private void loadRestaurants() {
        loadRestaurants(new DatabaseHelper());
    }

    private void loadRestaurants(DatabaseHelper db) {
        List<RestaurantOwner> owners = db.getAllRestaurantOwners();
        ObservableList<RestaurantOwner> data = FXCollections.observableArrayList(owners);
        restaurantsTable.setItems(data);
        if (totalRestaurantsLabel != null)
            totalRestaurantsLabel.setText("Total Registered Restaurants: " + data.size());
    }

    @FXML
    private void handleLogout() {
        navigateTo("/com/example/bhojhon/main-menu-view.fxml");
    }
}
