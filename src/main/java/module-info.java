module org.example.cpmethod {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.web;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;

    opens org.example.cpmethod.controllers to javafx.fxml;
    opens org.example.cpmethod to javafx.fxml;

    exports org.example.cpmethod;
}
