package org.example.cpmethod.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.cpmethod.GlobalData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MediatorController {

    @FXML private GridPane dataGrid;
    @FXML private ImageView logoImageView;
    @FXML private StackPane overlayPane;
    @FXML private VBox formVBox;
    @FXML private TextField supplierCountField;
    @FXML private TextField receiverCountField;
    @FXML private Button submitInitialBtn;
    @FXML private ImageView helpIcon;

    @FXML
    public void initialize() {
        generateInputMatrix(2, 3); // default view

        Platform.runLater(() -> {
            Scene scene = formVBox.getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/org/example/cpmethod/css/style.css").toExternalForm());
            }

            try {
                Image logo = new Image(getClass().getResource("/org/example/cpmethod/icons/logo.png").toExternalForm());
                logoImageView.setImage(logo);
                logoImageView.setCursor(javafx.scene.Cursor.HAND);
                logoImageView.setOnMouseClicked(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cpmethod/fxml/menu.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) logoImageView.getScene().getWindow();
                        stage.setScene(new Scene(root));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                System.err.println("Nie można załadować logo.png: " + e.getMessage());
            }

            try {
                Image image = new Image(getClass().getResource("/org/example/cpmethod/icons/help.jpg").toExternalForm());
                helpIcon.setImage(image);
                helpIcon.setFitWidth(24);
                helpIcon.setFitHeight(24);
                helpIcon.setPreserveRatio(true);
                helpIcon.setStyle("-fx-cursor: hand;");
                helpIcon.setClip(new javafx.scene.shape.Circle(
                        helpIcon.getFitWidth() / 2,
                        helpIcon.getFitHeight() / 2,
                        Math.min(helpIcon.getFitWidth(), helpIcon.getFitHeight()) / 2
                ));
            } catch (Exception e) {
                System.err.println("Nie można załadować help.jpg: " + e.getMessage());
            }

            helpIcon.setOnMouseClicked(this::showLegendDialog);
        });

        formVBox.setEffect(new javafx.scene.effect.GaussianBlur(10));

        submitInitialBtn.setOnAction(event -> {
            String suppliersText = supplierCountField.getText();
            String receiversText = receiverCountField.getText();

            if (suppliersText.matches("\\d+") && receiversText.matches("\\d+")) {
                int suppliers = Integer.parseInt(suppliersText);
                int receivers = Integer.parseInt(receiversText);

                overlayPane.setVisible(false);
                overlayPane.setManaged(false);
                formVBox.setEffect(null);

                generateInputMatrix(suppliers, receivers);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid numbers.");
                alert.showAndWait();
            }
        });
    }

    private void showLegendDialog(MouseEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("💡 Tips");
        alert.setHeaderText("Mediator Input Legend:");

        VBox content = new VBox(10,
                new Label("""
                    • Enter the number of suppliers and customers first.
                    • 'Supply ↓' – how many units each supplier can offer.
                    • 'Demand →' – how many units each customer needs.
                    • 'Unit Cost' – transport cost from a supplier to a customer.
                    • 'Purchase ↓' – cost of buying one unit from the supplier.
                    • 'Selling Price →' – price at which you sell to the customer.
                    • Press 'Solve' to compute the optimal transportation plan.
                    """)
        );
        content.setStyle("-fx-padding: 10;");
        alert.getDialogPane().setContent(content);
        alert.showAndWait();
    }

    private void generateInputMatrix(int suppliers, int customers) {
        dataGrid.getChildren().clear();
        dataGrid.getColumnConstraints().clear();
        dataGrid.setHgap(10);
        dataGrid.setVgap(10);

        // Column sizing
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setMinWidth(100); // Labels

        ColumnConstraints supplyCol = new ColumnConstraints();
        supplyCol.setHgrow(Priority.ALWAYS);

        dataGrid.getColumnConstraints().add(col0); // Label
        dataGrid.getColumnConstraints().add(supplyCol); // Supply

        for (int j = 0; j < customers; j++) {
            ColumnConstraints customerCol = new ColumnConstraints();
            customerCol.setHgrow(Priority.ALWAYS);
            dataGrid.getColumnConstraints().add(customerCol);
        }

        ColumnConstraints purchaseCol = new ColumnConstraints();
        purchaseCol.setHgrow(Priority.ALWAYS);
        dataGrid.getColumnConstraints().add(purchaseCol);

        // Header row
        dataGrid.add(new Label(""), 0, 0);

        Label supplyHeader = new Label("Supply ↓");
        supplyHeader.getStyleClass().add("header-label");
        dataGrid.add(supplyHeader, 1, 0);

        for (int j = 0; j < customers; j++) {
            Label customerLabel = new Label("Customer " + (j + 1));
            customerLabel.getStyleClass().add("header-label");
            dataGrid.add(customerLabel, j + 2, 0);
        }

        Label purchaseHeader = new Label("Purchase ↓");
        purchaseHeader.getStyleClass().add("header-label");
        dataGrid.add(purchaseHeader, customers + 2, 0);

        // Demand row
        Label demandLabel = new Label("Demand →");
        demandLabel.getStyleClass().add("header-label");
        dataGrid.add(demandLabel, 0, 1);
        dataGrid.add(new Label(""), 1, 1);

        for (int j = 0; j < customers; j++) {
            TextField demandField = new TextField();
            demandField.setPromptText("demand");
            demandField.getStyleClass().add("uniform-input");
            demandField.setId("demand" + (j + 1) + "Field");
            dataGrid.add(demandField, j + 2, 1);
        }

        dataGrid.add(new Label(""), customers + 2, 1);

        // Supplier rows
        for (int i = 0; i < suppliers; i++) {
            int row = i + 2;

            Label supplierLabel = new Label("Supplier " + (i + 1));
            supplierLabel.getStyleClass().add("header-label");
            dataGrid.add(supplierLabel, 0, row);

            TextField supplyField = new TextField();
            supplyField.setPromptText("supply");
            supplyField.getStyleClass().add("uniform-input");
            supplyField.setId("supply" + (i + 1) + "Field");
            dataGrid.add(supplyField, 1, row);

            for (int j = 0; j < customers; j++) {
                TextField costField = new TextField();
                costField.setPromptText("unit cost");
                costField.getStyleClass().addAll("uniform-input", "matrix-cell");
                costField.setId("c" + (j + 1) + "s" + (i + 1) + "Field");
                dataGrid.add(costField, j + 2, row);
            }

            TextField purchaseField = new TextField();
            purchaseField.setPromptText("purchase ");
            purchaseField.getStyleClass().add("uniform-input");
            purchaseField.setId("purchase" + (i + 1) + "Field");
            dataGrid.add(purchaseField, customers + 2, row);
        }

        // Selling Price row
        int sellingRow = suppliers + 2;
        Label sellLabel = new Label("Selling Price →");
        sellLabel.getStyleClass().add("header-label");
        dataGrid.add(sellLabel, 0, sellingRow);
        dataGrid.add(new Label(""), 1, sellingRow);

        for (int j = 0; j < customers; j++) {
            TextField sellField = new TextField();
            sellField.setPromptText("sell");
            sellField.getStyleClass().add("uniform-input");
            sellField.setId("sell" + (j + 1) + "Field");
            dataGrid.add(sellField, j + 2, sellingRow);
        }

        // Solve Button
        Button solveButton = new Button("Solve");
        solveButton.setPrefWidth(100);
        solveButton.getStyleClass().add("solve-button");
        dataGrid.add(solveButton, customers + 2, sellingRow + 1);

        solveButton.setOnAction(event -> {
            try {
                String json = collectDataAsJson(suppliers, customers);
                if (json != null) {
                    sendToBackend(json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private javafx.scene.Node getNodeFromGridPane(GridPane grid, int col, int row) {
        for (javafx.scene.Node node : grid.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);
            if (colIndex != null && rowIndex != null && colIndex == col && rowIndex == row) {
                return node;
            }
        }
        return null;
    }

    private String collectDataAsJson(int suppliers, int customers) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ObjectNode clients = mapper.createObjectNode();
        ObjectNode producers = mapper.createObjectNode();

        var demand = mapper.createArrayNode();
        var clientPrices = mapper.createArrayNode();

        try {
            for (int j = 0; j < customers; j++) {
                TextField demandField = (TextField) getNodeFromGridPane(dataGrid, j + 2, 1);
                TextField sellField = (TextField) getNodeFromGridPane(dataGrid, j + 2, suppliers + 2);

                String demandText = demandField.getText().trim();
                String sellText = sellField.getText().trim();

                if (!demandText.matches("\\d+") || !sellText.matches("\\d+")) {
                    showValidationError("Please enter valid numbers in Demand or Selling Price fields.");
                    throw new IllegalArgumentException("Invalid number input.");
                }

                demand.add(Integer.parseInt(demandText));
                clientPrices.add(Integer.parseInt(sellText));
            }

            clients.set("demand", demand);
            clients.set("prices", clientPrices);

            var supply = mapper.createArrayNode();
            var producerPrices = mapper.createArrayNode();
            var cost = mapper.createArrayNode();

            for (int i = 0; i < suppliers; i++) {
                TextField supplyField = (TextField) getNodeFromGridPane(dataGrid, 1, i + 2);
                TextField priceField = (TextField) getNodeFromGridPane(dataGrid, customers + 2, i + 2);

                String supplyText = supplyField.getText().trim();
                String priceText = priceField.getText().trim();

                if (!supplyText.matches("\\d+") || !priceText.matches("\\d+")) {
                    showValidationError("Please enter valid numbers in Supply or Purchase fields.");
                    throw new IllegalArgumentException("Invalid number input.");
                }

                supply.add(Integer.parseInt(supplyText));
                producerPrices.add(Integer.parseInt(priceText));

                var row = mapper.createArrayNode();
                for (int j = 0; j < customers; j++) {
                    TextField costField = (TextField) getNodeFromGridPane(dataGrid, j + 2, i + 2);
                    String costText = costField.getText().trim();

                    if (!costText.matches("\\d+")) {
                        showValidationError("Please enter valid unit costs (only numbers).");
                        throw new IllegalArgumentException("Invalid cost value.");
                    }

                    row.add(Integer.parseInt(costText));
                }
                cost.add(row);
            }

            producers.set("supply", supply);
            producers.set("prices", producerPrices);
            root.set("clients", clients);
            root.set("producers", producers);
            root.set("cost", cost);

        } catch (IllegalArgumentException e) {
            return null;
        }

        return root.toPrettyString();
    }
    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("❌ Input Validation Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void sendToBackend(String jsonBody) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://laptop.skaner.dev/transport"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> Platform.runLater(() -> {
                    try {
                        GlobalData.getInstance().setSolutionJson(response);

                        // ✅ NEW: show confirmation dialog instead of switching scene
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("✅ Solution has been successfully generated!");
                        alert.showAndWait();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }))
                .exceptionally(e -> {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText("Backend communication failed");
                        errorAlert.setContentText("❌ Could not generate the solution. Check your network or backend availability.");
                        errorAlert.showAndWait();
                    });
                    return null;
                });
    }

}
