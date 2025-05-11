package org.example.cpmethod.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.example.cpmethod.AppData;

public class DiagramController {

    @FXML
    private ImageView helpIcon;
    @FXML private WebView svgViewer;
    @FXML
    private void initialize() {
        if (AppData.backendResponse != null) {
            svgViewer.getEngine().load(AppData.backendResponse.getDiagramUrl());
        }
        // Wczytanie ikony pomocy
        try {
            Image image = new Image(getClass().getResource("/org/example/cpmethod/icons/help.jpg").toExternalForm());
            helpIcon.setImage(image);
            helpIcon.setFitWidth(24);
            helpIcon.setFitHeight(24);
            helpIcon.setPreserveRatio(true);
            helpIcon.setStyle("-fx-cursor: hand;");

            // Zaokrąglenie do okręgu
            helpIcon.setClip(new javafx.scene.shape.Circle(
                    helpIcon.getFitWidth() / 2,
                    helpIcon.getFitHeight() / 2,
                    Math.min(helpIcon.getFitWidth(), helpIcon.getFitHeight()) / 2
            ));

        } catch (Exception e) {
            System.err.println("Nie można załadować help.jpg: " + e.getMessage());
        }

        // Obsługa kliknięcia
        helpIcon.setOnMouseClicked(this::showLegendDialog);
    }

    private void showLegendDialog(MouseEvent event) {
        // Wczytanie obrazka z legendą diagramu
        ImageView diagramImage = new ImageView();
        try {
            Image image = new Image(getClass().getResource("/org/example/cpmethod/icons/diagram_legend.png").toExternalForm());
            diagramImage.setImage(image);
            diagramImage.setFitWidth(200);
            diagramImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Nie można załadować diagram_legend.png: " + e.getMessage());
        }

        // Wyświetlenie legendy
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("💡 Tips");
        alert.setHeaderText("Diagram Legend:");

        VBox content = new VBox(10,
                new Label("""
        • ES – Early Start
        • EF – Early Finish
        • LS – Late Start
        • LF – Late Finish
        • T – Duration
        • R – Time Reserve
    """),
                diagramImage
        );
        content.setStyle("-fx-padding: 10;");

        alert.getDialogPane().setContent(content);
        alert.showAndWait();
    }
}
