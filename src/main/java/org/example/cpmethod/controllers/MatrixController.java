package org.example.cpmethod.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.example.cpmethod.GlobalData;

public class MatrixController {

    @FXML private GridPane matrixGrid;
    @FXML private Slider stepSlider;
    @FXML private Label stepLabel;
    @FXML private Label incomeLabel;
    @FXML private Label purchaseCostLabel;
    @FXML private Label transportCostLabel;
    @FXML private Label totalCostLabel;
    @FXML private Label profitLabel;
    @FXML private ImageView helpIcon;

    private JsonNode history;
    private int[][] costs;

    @FXML
    public void initialize() {
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

        helpIcon.setOnMouseClicked(event -> showHelpDialog());


        try {
            String json = GlobalData.getInstance().getSolutionJson();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            JsonNode costMatrixNode = root.get("cost_matrix");
            JsonNode transportMatrixNode = root.get("transport_matrix");
            JsonNode params = root.get("parameters");

            int rows = costMatrixNode.size();
            int cols = costMatrixNode.get(0).size();

            costs = new int[rows][cols];
            int[][] transports = new int[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    costs[i][j] = costMatrixNode.get(i).get(j).asInt();
                    transports[i][j] = transportMatrixNode.get(i).get(j).asInt();
                }
            }

            int suppliers = rows - 1;
            int receivers = cols - 1;

            generateResultMatrix(suppliers, receivers, costs, transports);

            // Set parameter values
            if (params != null) {
                incomeLabel.setText(params.get("pc").asText() + " zł");
                purchaseCostLabel.setText(params.get("kz").asText() + " zł");
                transportCostLabel.setText(params.get("kt").asText() + " zł");
                totalCostLabel.setText(params.get("kc").asText() + " zł");
                profitLabel.setText(params.get("zc").asText() + " zł");
            }

            // Handle history for step navigation
            history = root.get("history");
            if (history != null && history.isArray() && history.size() > 0) {
                stepSlider.setMin(1);
                stepSlider.setMax(history.size());
                stepSlider.setValue(1);
                stepLabel.setText("Step 1 of " + history.size());

                stepSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                    int step = newVal.intValue() - 1;
                    if (step >= 0 && step < history.size()) {
                        JsonNode stepNode = history.get(step);
                        int[][] transportStep = new int[rows][cols];

                        for (int i = 0; i < rows; i++) {
                            for (int j = 0; j < cols; j++) {
                                transportStep[i][j] = stepNode.get("transport_matrix").get(i).get(j).asInt();
                            }
                        }

                        stepLabel.setText("Step " + (step + 1) + " of " + history.size());
                        generateResultMatrix(suppliers, receivers, costs, transportStep);
                    }
                });
            } else {
                stepLabel.setText("No steps available");
                stepSlider.setDisable(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("💡 Tip");
        alert.setHeaderText("How to use the history slider");
        alert.setContentText("""
        You can drag the slider to preview each step 
        of the matrix calculation history.
        
        Each step shows how the transport matrix evolved 
        during the optimization process.
        """);
        alert.showAndWait();
    }

    public void generateResultMatrix(int suppliers, int receivers, int[][] costs, int[][] transports) {
        matrixGrid.getChildren().clear();
        matrixGrid.getColumnConstraints().clear();
        matrixGrid.getRowConstraints().clear();

        int totalRows = suppliers + 2;
        int totalCols = receivers + 2;

        for (int col = 1; col < totalCols; col++) {
            String headerText = (col <= receivers) ? "O" + col : "OF";
            Label header = new Label(headerText);
            header.getStyleClass().add("header-label");
            matrixGrid.add(header, col, 0);
        }

        for (int row = 1; row < totalRows; row++) {
            String headerText = (row <= suppliers) ? "D" + row : "DF";
            Label rowHeader = new Label(headerText);
            rowHeader.getStyleClass().add("header-label");
            matrixGrid.add(rowHeader, 0, row);
        }

        for (int i = 0; i < totalRows - 1; i++) {
            for (int j = 0; j < totalCols - 1; j++) {
                int cost = costs[i][j];
                int amount = transports[i][j];

                Label costLabel = new Label(String.valueOf(cost));
                costLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
                AnchorPane.setTopAnchor(costLabel, 2.0);
                AnchorPane.setLeftAnchor(costLabel, 4.0);

                Label amountBox = new Label(amount > 0 ? String.valueOf(amount) : "x");
                amountBox.setPrefSize(20, 20);
                amountBox.setStyle("""
                        -fx-border-color: white;
                        -fx-border-width: 1;
                        -fx-text-fill: white;
                        -fx-font-size: 14px;
                        -fx-alignment: center;
                    """);
                AnchorPane.setBottomAnchor(amountBox, 0.0);
                AnchorPane.setRightAnchor(amountBox, 0.0);

                AnchorPane cellPane = new AnchorPane(costLabel, amountBox);
                cellPane.setPrefSize(65, 50);
                cellPane.setStyle("-fx-border-color: gray; -fx-background-color: #111827;");
                matrixGrid.add(cellPane, j + 1, i + 1);
            }
        }
    }
}
