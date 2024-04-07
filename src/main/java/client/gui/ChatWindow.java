package client.gui;

import client.networking.ClientOutput;
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
 * the controller is specified in the FXML and an instance of the class CWcontroller is created when loading
 */
public class ChatWindow extends Application  implements Runnable {
    Stage window;
    //private static ClientOutput networkManager;
    CWcontroller controller;

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            this.window = primaryStage;

            //this.address = super.getParameters().getRaw().get(0);
            //this.port = Integer.parseInt(super.getParameters().getRaw().get(1));
            // Specify scene, here scene is loaded from FXML
            URL url = new File("src/main/resources/chatwindow.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatwindow.fxml"));
            Parent root = (Parent)loader.load();//(getClass().getResource("src/main/resources/chatwindow.fxml"));
            CWcontroller cwController = (CWcontroller)loader.getController();
            Scene scene = new Scene(root, 600, 300, Color.BLACK);

            //set controller
            this.controller = loader.getController();

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

    @Override
    public void run(){
        launch();
    }

    //public static void setNetworkManager(ClientOutput clientOutput){
        //ChatWindow.networkManager = clientOutput;
        //System.out.println("Network Manager set: " + (ChatWindow.networkManager).toString());
    //}

    //public static ClientOutput getNetworkManager(){
        //return networkManager;
    //}




}
