package com.example.bhojhon.controller;

import com.example.bhojhon.data.DatabaseHelper;
import com.example.bhojhon.model.OrderInfo;
import com.example.bhojhon.util.BaseController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminDashboardController extends BaseController {

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

    @Override
    public void initialize() {
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("passengerName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        trainCol.setCellValueFactory(new PropertyValueFactory<>("trainNumber"));
        seatCol.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        stationCol.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        itemsCol.setCellValueFactory(new PropertyValueFactory<>("orderItems"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        loadData();
    }

    @FXML
    private void loadData() {
        DatabaseHelper db = new DatabaseHelper();
        ordersTable.setItems(FXCollections.observableArrayList(db.getAllOrdersInfo()));
    }

    @FXML
    private void handleLogout() {
        navigateTo("/com/example/bhojhon/main-menu-view.fxml");
    }
}
