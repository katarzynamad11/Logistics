package org.example.cpmethod.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MenuController {

    @FXML
    private Button cpmButton;

    @FXML
    private ImageView Metoda_CPM_ImageView;
    @FXML
    private ImageView Zagadnienie_posrednika_ImageView;
    @FXML private AnchorPane anchorPane;

    @FXML
    public void initialize() {
        cpmButton.setOnAction(this::handleCPM);


        Image Metoda_CPM = new Image(getClass().getResource("/org/example/cpmethod/icons/CP.png").toExternalForm());
        Metoda_CPM_ImageView.setImage(Metoda_CPM);

        Image Zagadnienie_posrednika = new Image(getClass().getResource("/org/example/cpmethod/icons/POs.png").toExternalForm());
        Zagadnienie_posrednika_ImageView.setImage(Zagadnienie_posrednika);

        //  Tutaj ustawiamy tło
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(getClass().getResource("/org/example/cpmethod/icons/centered_line.png").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1100, 700, false, false, false, false)
        );
        anchorPane.setBackground(new Background(backgroundImage));
    }


    private void handleCPM(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cpmethod/fxml/cpm.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1100, 700);
            scene.getStylesheets().add(getClass().getResource("/org/example/cpmethod/css/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Metoda CPM");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
