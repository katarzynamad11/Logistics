package org.example.cpmethod.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import org.example.cpmethod.AppData;
import org.example.cpmethod.ResultData;
import org.example.cpmethod.ResultRow;

/**
 * Controller odpowiedzialny za wyświetlanie wyników z backendu w tabeli.
 * Wczytuje dane z AppData.backendResponse i mapuje do modelu ResultData (przystosowanego do TableView).
 */
public class TableController {

    @FXML private TableView<ResultData> resultTable;
    @FXML private TableColumn<ResultData, String> colActivity;
    @FXML private TableColumn<ResultData, String> colDuration;
    @FXML private TableColumn<ResultData, String> colES;
    @FXML private TableColumn<ResultData, String> colEF;
    @FXML private TableColumn<ResultData, String> colLS;
    @FXML private TableColumn<ResultData, String> colLF;
    @FXML private TableColumn<ResultData, String> colSlack;
    @FXML private TableColumn<ResultData, String> colCritical;

    @FXML private ImageView helpIcon;

    @FXML
    private void initialize() {
        resultTable.setEditable(false); // Wyłączam edytowalność - bo backend generuje dane

        // Powiązania kolumn z właściwościami ResultData
        colActivity.setCellValueFactory(new PropertyValueFactory<>("activity"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colES.setCellValueFactory(new PropertyValueFactory<>("es"));
        colEF.setCellValueFactory(new PropertyValueFactory<>("ef"));
        colLS.setCellValueFactory(new PropertyValueFactory<>("ls"));
        colLF.setCellValueFactory(new PropertyValueFactory<>("lf"));
        colSlack.setCellValueFactory(new PropertyValueFactory<>("slack"));
        colCritical.setCellValueFactory(new PropertyValueFactory<>("critical"));

        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Ładuj dane tylko z backendu (z AppData)
        if (AppData.backendResponse != null) {
            ObservableList<ResultData> tableData = FXCollections.observableArrayList();
            for (ResultRow row : AppData.backendResponse.getResults()) {
                tableData.add(new ResultData(
                        row.getTask(),
                        String.valueOf(row.getDuration()),
                        String.valueOf(row.getEarliestStart()),
                        String.valueOf(row.getEarliestFinish()),
                        String.valueOf(row.getLatestStart()),
                        String.valueOf(row.getLatestFinish()),
                        String.valueOf(row.getSlack()),
                        row.isCritical() ? "Yes" : "No"
                ));
            }
            resultTable.setItems(tableData);
        }

        setupHelpIcon();
    }

    private void setupHelpIcon() {
        try {
            Image image = new Image(getClass().getResource("/org/example/cpmethod/icons/help.jpg").toExternalForm());
            helpIcon.setImage(image);
            helpIcon.setFitWidth(24);
            helpIcon.setFitHeight(24);
            helpIcon.setPreserveRatio(true);
            helpIcon.setStyle("-fx-cursor: hand;");
            helpIcon.setClip(new Circle(12, 12, 12));
            helpIcon.setOnMouseClicked(event -> showLegendDialog());
        } catch (Exception e) {
            System.err.println("Unable to load help.jpg: " + e.getMessage());
        }
    }

    private void showLegendDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("💡 Tips");
        alert.setHeaderText("Table Field Descriptions:");
        alert.setContentText("""
            • ES – Early Start
            • EF – Early Finish
            • LS – Late Start
            • LF – Late Finish
            • Float – Time reserve available for an activity
            • Critical Activity – When Float = 0
            """);
        alert.showAndWait();
    }
}
