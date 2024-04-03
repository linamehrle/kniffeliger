module application.client.gui {
    requires javafx.controls;
    requires javafx.fxml;

    opens application.client.gui to javafx.fxml;
    exports application.client.gui;
}