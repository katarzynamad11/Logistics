package org.example.cpmethod.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.example.cpmethod.AppData;
import org.example.cpmethod.BackendResponse;
import org.example.cpmethod.EventData;
import org.example.cpmethod.ResultRow;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class CPMController {

    @FXML private TextField eventNameField;
    @FXML private TextField eventDurationField;
    @FXML private TextField eventFromField;
    @FXML private TableView<EventData> eventsTable;
    @FXML
    private ImageView helpIcon;
    private final ObservableList<EventData> eventDataList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        eventsTable.setEditable(true); // edytowanie

        TableColumn<EventData, String> colLp = (TableColumn<EventData, String>) eventsTable.getColumns().get(0);
        TableColumn<EventData, String> colName = (TableColumn<EventData, String>) eventsTable.getColumns().get(1);
        TableColumn<EventData, String> colDuration = (TableColumn<EventData, String>) eventsTable.getColumns().get(2);
        TableColumn<EventData, String> colSuccessors = (TableColumn<EventData, String>) eventsTable.getColumns().get(3);

        colLp.setCellValueFactory(cellData -> cellData.getValue().lpProperty());
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colDuration.setCellValueFactory(cellData -> cellData.getValue().durationProperty());
        colSuccessors.setCellValueFactory(cellData -> cellData.getValue().successorsProperty());

        colLp.setPrefWidth(35);
        colLp.setMaxWidth(35);
        colLp.setMinWidth(35);

        // Ustaw komórki jako edytowalne
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(event -> {
            EventData row = event.getRowValue();
            row.setName(event.getNewValue());
        });

        colDuration.setCellFactory(TextFieldTableCell.forTableColumn());
        colDuration.setOnEditCommit(event -> {
            EventData row = event.getRowValue();
            row.setDuration(event.getNewValue());
        });

        colSuccessors.setCellFactory(TextFieldTableCell.forTableColumn());
        colSuccessors.setOnEditCommit(event -> {
            EventData row = event.getRowValue();
            row.setSuccessors(event.getNewValue());
        });

        // Przypisz dane i reszta
        eventsTable.setItems(eventDataList);
        eventsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        addDeleteButtonToTable();

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

    @FXML
    private void handleAddEvent(ActionEvent event) {
        String name = eventNameField.getText().trim();
        String duration = eventDurationField.getText().trim();
        String from = eventFromField.getText().trim();

        if (name.isEmpty() || duration.isEmpty() || from.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Please fill in all fields.\nIf there is no predecessor, use '-' as a placeholder.");
            return;
        }

        try {
            Integer.parseInt(duration);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Duration must be a number.");
            return;
        }


        int lp = eventDataList.size() + 1;
        EventData newEvent = new EventData(String.valueOf(lp), name, duration, from);
        eventDataList.add(newEvent);
        clearForm();
    }

    // Button - Solve
    @FXML
    private void handleGenerateDiagram() {
        try {
            ObservableList<EventData> rows = eventsTable.getItems();
            Map<String, Object> tasksMap = new HashMap<>();

            for (EventData row : rows) {
                String name = row.getName().trim();
                int duration = Integer.parseInt(row.getDuration().trim());

                List<String> previous = new ArrayList<>();
                String rawPredecessors = row.getSuccessors().trim();

                if (!rawPredecessors.equals("-") && !rawPredecessors.isEmpty()) {
                    previous = Arrays.stream(rawPredecessors.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty() && !s.equals("-"))
                            .toList();
                }

                Map<String, Object> taskDetails = new HashMap<>();
                taskDetails.put("duration", duration);
                taskDetails.put("previous", previous);

                tasksMap.put(name, taskDetails);
            }

            // Przygotowanie JSON-a
            Map<String, Object> payload = Map.of("tasks", tasksMap);
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(payload);
            System.out.println("✅ JSON wysyłany do backendu:");
            System.out.println(jsonBody);

            // Wysyłka do backendu (POST)
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://webhook.site/8bf0c237-9141-4b5f-b182-30be320665f7"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("✅ Response from backend (POST):");
            System.out.println(postResponse.body());

            // Pobranie SVG linków (GET)
            HttpRequest svgsRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:3000/svgs"))
                    .GET()
                    .build();

            HttpResponse<String> svgsResponse = client.send(svgsRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("✅ Received SVG links:");
            System.out.println(svgsResponse.body());

            Map<String, String> svgsMap = mapper.readValue(svgsResponse.body(), Map.class);

            // Pobranie wyników do tabeli (GET)
            HttpRequest resultsRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:3000/results"))
                    .GET()
                    .build();

            HttpResponse<String> resultsResponse = client.send(resultsRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println("✅ Received results:");
            System.out.println(resultsResponse.body());

            List<ResultRow> resultsList = Arrays.asList(mapper.readValue(resultsResponse.body(), ResultRow[].class));

            // Pakowanie wszystkiego razem do BackendResponse
            BackendResponse backendData = new BackendResponse(
                    svgsMap.get("gantt"),
                    svgsMap.get("diagram"),
                    resultsList
            );

            AppData.backendResponse = backendData;

            // Komunikat dla użytkownika
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("✅ Calculation Complete");
            successAlert.setHeaderText("Data has been successfully sent and fetched from backend!");
            successAlert.setContentText("You can now view:\n• Diagram\n• Gantt chart\n• Table");
            successAlert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("❌ Error");
            errorAlert.setHeaderText("An error occurred during backend communication.");
            errorAlert.setContentText("Check if backend is running and try again.");
            errorAlert.showAndWait();
        }
    }





    private void clearForm() {
        eventNameField.clear();
        eventDurationField.clear();
        eventFromField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void addDeleteButtonToTable() {
        TableColumn<EventData, Void> deleteCol = new TableColumn<>("");
        deleteCol.setPrefWidth(30);
        deleteCol.setMaxWidth(42);
        deleteCol.setMinWidth(42);

        Callback<TableColumn<EventData, Void>, TableCell<EventData, Void>> cellFactory = param -> new TableCell<>() {
            private final Button deleteButton = new Button();

            {
                ImageView trashIcon = new ImageView(new Image(
                        getClass().getResourceAsStream("/org/example/cpmethod/icons/trash.png")));
                trashIcon.setFitHeight(16);
                trashIcon.setFitWidth(16);
                deleteButton.setGraphic(trashIcon);
                deleteButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                deleteButton.setOnAction(event -> {
                    EventData currentItem = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(currentItem);
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        };

        deleteCol.setCellFactory(cellFactory);
        eventsTable.getColumns().add(deleteCol);
    }





    @FXML
    private void showLegendDialog(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("💡 Tips");
        alert.setHeaderText("Table Field Descriptions & Usage Guide:");
        alert.setContentText("""
        — To calculate the diagrams (CPM or Gantt), you need to fill in the data by completing the form.
        — You can edit any record by double-clicking on it.
        — To delete a record, click the trash bin icon next to it.
        — Once all fields are filled in, click 'Solve' to generate the results.
        — If an event has multiple predecessors, separate them with commas (e.g., A,C).
    """);
        alert.showAndWait();
    }



}
