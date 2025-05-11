package org.example.cpmethod.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.example.cpmethod.AppData;

public class GanttController {

    @FXML private WebView svgViewer;

    @FXML
    private void initialize() {
        if (AppData.backendResponse != null) {
            svgViewer.getEngine().load(AppData.backendResponse.getGanttUrl());
        }
    }


    public void loadSvgFromUrl(String svgUrl) {
        WebEngine webEngine = svgViewer.getEngine();
        webEngine.load(svgUrl);
    }


}
