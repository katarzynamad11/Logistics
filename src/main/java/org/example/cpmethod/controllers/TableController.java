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
        resultTable.setEditable(true);

        // Ustaw kolumny
        setupColumn(colActivity, "activity");
        setupColumn(colDuration, "duration");
        setupColumn(colES, "es");
        setupColumn(colEF, "ef");
        setupColumn(colLS, "ls");
        setupColumn(colLF, "lf");
        setupColumn(colSlack, "slack");
        setupColumn(colCritical, "isCritical");

        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Jeśli AppData ma wyniki — pokaż je
        if (AppData.backendResponse != null) {
            ObservableList<ResultData> converted = FXCollections.observableArrayList();

            for (var r : AppData.backendResponse.getResults()) {
                converted.add(new ResultData(
                        r.getTask(),
                        String.valueOf(r.getDuration()),
                        String.valueOf(r.getEarliestStart()),
                        String.valueOf(r.getEarliestFinish()),
                        String.valueOf(r.getLatestStart()),
                        String.valueOf(r.getLatestFinish()),
                        String.valueOf(r.getSlack()),
                        r.isCritical() ? "Tak" : "Nie"
                ));
            }

            resultTable.setItems(converted);
        }

        // Ikona pomocy
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
            System.err.println("Nie można załadować help.jpg: " + e.getMessage());
        }
    }

    private void setupColumn(TableColumn<ResultData, String> column, String property) {
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            switch (property) {
                case "activity" -> event.getRowValue().setActivity(event.getNewValue());
                case "duration" -> event.getRowValue().setDuration(event.getNewValue());
                case "es" -> event.getRowValue().setEs(event.getNewValue());
                case "ef" -> event.getRowValue().setEf(event.getNewValue());
                case "ls" -> event.getRowValue().setLs(event.getNewValue());
                case "lf" -> event.getRowValue().setLf(event.getNewValue());
                case "slack" -> event.getRowValue().setSlack(event.getNewValue());
                case "isCritical" -> event.getRowValue().setIsCritical(event.getNewValue());
            }
        });
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
