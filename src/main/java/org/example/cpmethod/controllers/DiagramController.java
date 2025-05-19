package org.example.cpmethod.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;
import org.example.cpmethod.AppData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DiagramController {

    @FXML
    private WebView svgViewer;

    @FXML
    private ImageView helpIcon;

    @FXML
    private void initialize() {
        // Załaduj SVG z backendu
        if (AppData.backendResponse != null) {
            String diagramUrl = AppData.backendResponse.getDiagramUrl();
            loadSvgFromUrl(diagramUrl);
        }

        // Ustaw ikonę pomocy jako okrągłą
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

        // Zaokrąglenie rogów WebView
        double arcSize = 20.0;
        Rectangle clip = new Rectangle();
        clip.setArcWidth(arcSize);
        clip.setArcHeight(arcSize);
        clip.widthProperty().bind(svgViewer.widthProperty());
        clip.heightProperty().bind(svgViewer.heightProperty());
        svgViewer.setClip(clip);

        svgViewer.setStyle("-fx-background-radius: 20; -fx-border-radius: 20; -fx-background-color: transparent;");
    }

    private void loadSvgFromUrl(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Osadzenie SVG w HTML z tłem #E8ECF1 i powiększeniem 2x
            String svgContent = response.body();
            String htmlWrapper = """
                <!DOCTYPE html>
                <html>
                  <head>
                    <meta charset="UTF-8">
                    <style>
                      body {
                        margin: 0;
                        padding: 0;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                        background-color: #E8ECF1;
                        overflow: hidden;
                        border-radius: 20px;
                      }
                      svg {
                        max-width: 100%%;
                        height: auto;
                        transform-origin: center;
                      }
                    </style>
                  </head>
                  <body>
                    %s
                  </body>
                </html>
                """.formatted(svgContent);

            svgViewer.getEngine().loadContent(htmlWrapper, "text/html");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLegendDialog(MouseEvent event) {
        // Wczytanie obrazka legendy diagramu
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
