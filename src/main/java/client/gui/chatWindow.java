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

/**
 * This class implements the GUI for the chat functions (WHISPER, CHAT)
 */
public class ChatWindow extends Application {
    Stage window;
    String address;
    int port;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.window = primaryStage;


        // Specify scene
        Parent root = FXMLLoader.load(getClass().getResource("chatwindow.fxml"));
        Scene scene = new Scene(root, 300, 300, Color.BLACK);
        // Add stylesheet
        scene.getStylesheets().add("styles/chatWindow.css");

        // Show Window
        this.window.setTitle("Chat");
        this.window.setScene(scene);
        this.window.show();



    }
}
