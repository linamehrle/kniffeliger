package client.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import client.networking.ClientInput;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;
import client.networking.ConsoleInput;
import java.io.IOException;
import client.GameManager;

/**
 * This class implements and launches the framework of the GUI for the chat functions (WHISPER, CHAT)
 * The actions are implemented in the class CWcontroller
 */
public class ChatWindow extends Application {
    Stage window;
    String address;
    int port;

    @Override
    public void start(Stage primaryStage) throws IOException {

        this.window = primaryStage;
        this.address = super.getParameters().getRaw().get(0);
        this.port = Integer.parseInt(super.getParameters().getRaw().get(1));

        // Specify scene, here scene is loaded from FXML (TODO: add file not found error handling)
        Parent root = FXMLLoader.load(getClass().getResource("/main/resources/chatwindow.fxml"));
        Scene scene = new Scene(root, 300, 300, Color.BLACK);

        // Add stylesheet, will be used later (TODO: add file not found error handling)
        //scene.getStylesheets().add("/main/resources/styles/chatWindow.css");

        // Show Window
        this.window.setTitle("Chat");
        this.window.setScene(scene);
        this.window.show();



    }

    public static void main(String[] args) {
        launch(args);
    }
}
