module client.gui {
    requires javafx.controls;
    requires javafx.fxml;

    opens client.gui to javafx.fxml;
    exports client.gui;
}