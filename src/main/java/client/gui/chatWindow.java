package client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * This class implements and launches the framework of the GUI for the chat functions (WHISPER, CHAT)
 * The actions are implemented in the class CWcontroller
 */
public class ChatWindow extends Application {
    Stage window;
    private static CWcontroller chatController;
    String address;
    int port;

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            this.window = primaryStage;
            //this.address = super.getParameters().getRaw().get(0);
            //this.port = Integer.parseInt(super.getParameters().getRaw().get(1));
            // Specify scene, here scene is loaded from FXML (TODO: add file not found error handling)
            URL url = new File("src/main/resources/chatwindow.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);//(getClass().getResource("src/main/resources/chatwindow.fxml"));
            Scene scene = new Scene(root, 600, 300, Color.BLACK);

            // Add stylesheet, will be used later (TODO: add file not found error handling)
            //scene.getStylesheets().add("src/main/resources/styles/chatWindow.css");

            // Show Window
            this.window.setTitle("Chat");
            this.window.setScene(scene);
            this.window.show();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setCWcontroller(CWcontroller cwController) {
        ChatWindow.chatController = cwController;

    }
}
