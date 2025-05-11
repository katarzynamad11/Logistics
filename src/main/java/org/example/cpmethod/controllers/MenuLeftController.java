package org.example.cpmethod.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuLeftController {

    @FXML private Button menuButton;
    @FXML private Button cpmButton;
    @FXML private Button diagramButton;
    @FXML private Button ganttButton;
    @FXML private Button tableButton;
    @FXML private javafx.scene.image.ImageView logoImageView;


    @FXML
    private void initialize() {
        menuButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWindow, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        Stage stage = (Stage) newWindow;
                        updateActiveButton(stage.getTitle());

                        stage.titleProperty().addListener((o, oldTitle, newTitle) -> {
                            updateActiveButton(newTitle);
                        });
                    }
                });
            }
        });

        // Załaduj logo
        Platform.runLater(() -> {
            javafx.scene.image.Image logo = new javafx.scene.image.Image(getClass().getResource("/org/example/cpmethod/icons/logo.png").toExternalForm());
            logoImageView.setImage(logo);
        });
    }



    private void updateActiveButton(String title) {
        clearActiveButtons();

        if (title == null) return;

        if (title.contains("CPM")) setActiveButton(cpmButton);
        else if (title.contains("Diagram")) setActiveButton(diagramButton);
        else if (title.contains("Gantt")) setActiveButton(ganttButton);
        else if (title.contains("Table")) setActiveButton(tableButton);
        else setActiveButton(menuButton);
    }

    private void clearActiveButtons() {
        menuButton.getStyleClass().remove("active-button");
        cpmButton.getStyleClass().remove("active-button");
        diagramButton.getStyleClass().remove("active-button");
        ganttButton.getStyleClass().remove("active-button");
        tableButton.getStyleClass().remove("active-button");
    }

    private void setActiveButton(Button button) {
        if (!button.getStyleClass().contains("active-button")) {
            button.getStyleClass().add("active-button");
        }
    }

    @FXML
    private void handleMenuClick(ActionEvent event) {
        loadScene(event, "/org/example/cpmethod/fxml/menu.fxml", "Menu");
    }

    @FXML
    private void handleCPMClick(ActionEvent event) {
        loadScene(event, "/org/example/cpmethod/fxml/cpm.fxml", "CPM");
    }

    @FXML
    private void handleDiagramClick(ActionEvent event) {
        loadScene(event, "/org/example/cpmethod/fxml/diagram.fxml", "Diagram");
    }

    @FXML
    private void handleGanttClick(ActionEvent event) {
        loadScene(event, "/org/example/cpmethod/fxml/gantt.fxml", "Gantt");
    }

    @FXML
    private void handleTableClick(ActionEvent event) {
        loadScene(event, "/org/example/cpmethod/fxml/table.fxml", "Table");
    }

    private void loadScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1100, 700);
            scene.getStylesheets().add(getClass().getResource("/org/example/cpmethod/css/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
