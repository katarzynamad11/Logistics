package org.example.cpmethod.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class MedController {

    @FXML private Button mediatorButton;
    @FXML private Button menuButton;
    @FXML private Button matrixButton;
    @FXML private ImageView logoImageView;

    @FXML
    private void initialize() {
        mediatorButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
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

        Platform.runLater(() -> {
            matrixButton.setText("Solution"); // zmień etykietę przycisku
            Image logo = new Image(getClass()
                    .getResource("/org/example/cpmethod/icons/logo.png")
                    .toExternalForm());
            logoImageView.setImage(logo);
        });
    }

    private void updateActiveButton(String title) {
        clearActiveButtons();

        if (title == null) return;

        if (title.contains("Solution")) {
            setActiveButton(matrixButton);
        } else if (title.contains("Menu")) {
            setActiveButton(menuButton);
        } else {
            setActiveButton(mediatorButton);
        }
    }

    private void clearActiveButtons() {
        mediatorButton.getStyleClass().remove("active-button");
        menuButton.getStyleClass().remove("active-button");
        matrixButton.getStyleClass().remove("active-button");
    }

    private void setActiveButton(Button button) {
        if (!button.getStyleClass().contains("active-button")) {
            button.getStyleClass().add("active-button");
        }
    }

    @FXML
    private void handleMediatorClick(ActionEvent event) {
        loadScene(event, "/org/example/cpmethod/fxml/mediator.fxml", "Mediator Input");
    }

    @FXML
    private void handleMenuClick(ActionEvent event) {
        loadScene(event, "/org/example/cpmethod/fxml/menu.fxml", "Menu");
    }

    @FXML
    private void handleMatrixClick(ActionEvent event) {
        loadScene(event, "/org/example/cpmethod/fxml/matrix.fxml", "Solution");
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
