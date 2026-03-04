package com.example.bhojhon.controller;

import com.example.bhojhon.data.CartManager;
import com.example.bhojhon.data.DataManager;
import com.example.bhojhon.model.FoodItem;
import com.example.bhojhon.model.Restaurant;
import com.example.bhojhon.util.BaseController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class FoodMenuController extends BaseController {

    @FXML
    private Label restaurantInfoLabel;

    @FXML
    private TableView<FoodItem> menuTableView;

    @FXML
    private TableColumn<FoodItem, String> imageColumn; // <-- Image column

    @FXML
    private TableColumn<FoodItem, String> nameColumn;

    @FXML
    private TableColumn<FoodItem, String> categoryColumn;

    @FXML
    private TableColumn<FoodItem, String> descriptionColumn;

    @FXML
    private TableColumn<FoodItem, Double> priceColumn;

    @FXML
    private TableColumn<FoodItem, Void> actionColumn;

    @FXML
    private Label cartCountLabel;

    private Restaurant selectedRestaurant;

    @Override
    public void initialize() {
        selectedRestaurant = (Restaurant) navigationManager.getData("selectedRestaurant");

        if (selectedRestaurant != null) {
            restaurantInfoLabel.setText(selectedRestaurant.getName() + " - " + selectedRestaurant.getCuisine());

            setupImageColumn(); // Load images in the table
            setupOtherColumns(); // Name, category, description, price
            setupActionColumn(); // "Add to Cart" button

            // Load all food items for this restaurant
            var foodItems = DataManager.getInstance().getFoodItemsByRestaurantId(selectedRestaurant.getId());
            menuTableView.getItems().addAll(foodItems);
        }

        updateCartCount();
    }

    /** Setup image column to display network images */
    private void setupImageColumn() {
        // Bind image URL from FoodItem
        imageColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getImageUrl()));

        // Display images inside the table
        imageColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitHeight(60);
                imageView.setFitWidth(60);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null || imageUrl.isEmpty()) {
                    setGraphic(null);
                } else {
                    // <-- Replace this URL with your real image URL if needed
                    imageView.setImage(new Image(imageUrl, true));
                    setGraphic(imageView);
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }

    /** Setup name, category, description, and price columns */
    private void setupOtherColumns() {
        nameColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));

        categoryColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory()));

        descriptionColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));

        priceColumn.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getPrice()));

        // Format price with ৳
        priceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText((empty || price == null) ? null : String.format("৳%.0f", price));
            }
        });
    }

    /** Setup "Add to Cart" button for each row */
    private void setupActionColumn() {
        Callback<TableColumn<FoodItem, Void>, TableCell<FoodItem, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<FoodItem, Void> call(final TableColumn<FoodItem, Void> param) {
                return new TableCell<>() {
                    // Components for "Add" state
                    private final Button addBtn = new Button("Add");

                    // Components for "Quantity" state
                    private final Button minusBtn = new Button("-");
                    private final Button plusBtn = new Button("+");
                    private final Label qtyLabel = new Label();
                    private final javafx.scene.layout.HBox qtyPane = new javafx.scene.layout.HBox(10, minusBtn,
                            qtyLabel, plusBtn);

                    {
                        // Add Button Style
                        addBtn.setStyle("-fx-background-color: linear-gradient(to right, #1A73E8, #4285F4); " +
                                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-min-width: 80px;");
                        addBtn.setOnAction(event -> {
                            FoodItem foodItem = getTableRow().getItem();
                            if (foodItem != null) {
                                addToCart(foodItem);
                            }
                        });

                        // Qty Pane Style
                        qtyPane.setAlignment(Pos.CENTER);
                        String btnStyle = "-fx-min-width: 30px; -fx-background-radius: 50%; -fx-font-weight: bold;";
                        minusBtn.setStyle(btnStyle + "-fx-background-color: #ffebee; -fx-text-fill: #c62828;");
                        plusBtn.setStyle(btnStyle + "-fx-background-color: #e8f5e9; -fx-text-fill: #2e7d32;");
                        qtyLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                        minusBtn.setOnAction(event -> {
                            FoodItem item = getTableRow().getItem();
                            if (item != null) {
                                updateQuantityInCart(item, -1);
                            }
                        });
                        plusBtn.setOnAction(event -> {
                            FoodItem item = getTableRow().getItem();
                            if (item != null) {
                                updateQuantityInCart(item, 1);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        FoodItem foodItem = getTableRow() == null ? null : (FoodItem) getTableRow().getItem();
                        if (empty || foodItem == null) {
                            setGraphic(null);
                        } else {
                            int quantity = CartManager.getInstance().getQuantity(foodItem);

                            if (quantity > 0) {
                                qtyLabel.setText(String.valueOf(quantity));
                                setGraphic(qtyPane);
                            } else {
                                setGraphic(addBtn);
                            }
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
            }
        };
        actionColumn.setCellFactory(cellFactory);
    }

    /** Update quantity helper */
    private void updateQuantityInCart(FoodItem foodItem, int delta) {
        CartManager.getInstance().addItem(foodItem, delta);
        updateCartCount();
        menuTableView.refresh(); // Refresh to update buttons
    }

    /** Add item to cart */
    private void addToCart(FoodItem foodItem) {
        CartManager.getInstance().addItem(foodItem, 1);
        updateCartCount();
        menuTableView.refresh(); // Refresh to show qty controls
        // showAlert("Added to cart: " + foodItem.getName(),
        // Alert.AlertType.INFORMATION); // Removed alert for smoother flow
    }

    /** Update cart count display */
    private void updateCartCount() {
        int count = CartManager.getInstance().getItemCount();
        cartCountLabel.setText("Cart: " + count + " items");
    }

    @FXML
    private void handleViewCart() {
        if (CartManager.getInstance().isEmpty()) {
            showAlert("Your cart is empty", Alert.AlertType.WARNING);
            return;
        }
        goToCart();
    }

    @FXML
    private void handleBack() {
        goBack();
    }

    /** Show alert dialog */
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Food Menu");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
