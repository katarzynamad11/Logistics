package org.example.cpmethod.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import org.example.cpmethod.AppData;

public class GanttController {

    @FXML
    private WebView asapViewer;
    @FXML
    private WebView alapViewer;

    @FXML
    private void initialize() {
        if (AppData.backendResponse != null) {
            if (AppData.backendResponse.getGanttUrl() != null) {
                loadSvgIntoViewer(asapViewer, AppData.backendResponse.getGanttUrl());
            }
            if (AppData.backendResponse.getGanttALAPUrl() != null) {
                loadSvgIntoViewer(alapViewer, AppData.backendResponse.getGanttALAPUrl());
            }
        }
    }

    private void loadSvgIntoViewer(WebView webView, String svgUrl) {
        if (svgUrl != null && !svgUrl.isEmpty()) {
            String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    html, body {
                        margin: 0;
                        padding: 0;
                        background-color: #000A17;
                        height: 100%%;
                        width: 100%%;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        overflow: hidden;
                    }
                    .zoom-container {
                        transform: scale(2.0);
                        transform-origin: center;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                    }
                    img {
                        max-width: 100%%;
                        height: auto;
                        display: block;
                    }
                </style>
            </head>
            <body>
                <div class="zoom-container">
                    <img src="%s" alt="Gantt Diagram"/>
                </div>
            </body>
            </html>
        """.formatted(svgUrl);

            webView.setStyle("-fx-background-color: #000A17;");
            webView.setContextMenuEnabled(false);
            webView.getEngine().loadContent(html);
        }
    }

}
