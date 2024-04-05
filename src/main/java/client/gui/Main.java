package client.gui;
import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static LobbyWindowController lobbyWindowController;

    @Override
    public void start(Stage stage) {
        try {
            URL url = new File("src/main/resources/LobbyWindow.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


    public static void addNewLobby(String name) {
        lobbyWindowController.addLobby(name);
    }

    public static void addNewPlayer(String lobbyAndPlayer) {
        lobbyWindowController.addPlayerToLobby(lobbyAndPlayer);
    }

    public static void lobbyList(String lobbies) {
        lobbyWindowController.initializeLobbyList(lobbies);
    }


    public static void setLobbyWindowController(LobbyWindowController lobbyWindowController) {
        Main.lobbyWindowController = lobbyWindowController;
    }
}


