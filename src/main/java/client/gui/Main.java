package client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;

public class Main extends Application {

    private static LobbyWindowController lobbyWindowController;
    private static CWcontroller cWcontroller;
    Stage mainWidow;
    Stage chatWindow;

    @Override
    public void start(Stage stage) {
        mainWidow = stage;
        SceneController.setMainWindow(mainWidow);
        try {
            /*URL url = new File("src/main/resources/LobbyWindow.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
             */

            //von dominique:
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/LobbyWindow.fxml"));
            Parent root = loader.load();

            mainWidow.setOnCloseRequest(e -> {
                //e.consume();
                exit();
            });
            mainWidow.setScene(new Scene(root, 600, 400));
            mainWidow.show();

            chatWindow = new Stage();
            //Dominique, aus class ChatWindow
            FXMLLoader loaderChat = new FXMLLoader(getClass().getResource("/chatwindow.fxml"));
            Parent rootChat = (Parent)loaderChat.load();
            Scene scene = new Scene(rootChat, 600, 300, Color.BLACK);

            this.chatWindow.setScene(scene);
            this.chatWindow.show();
            System.out.println("ChatWindow launched");
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

    public static void removePlayer(String lobbyAndPlayerName) {
        lobbyWindowController.removePlayerFromList(lobbyAndPlayerName);
    }


    public static void setLobbyWindowController(LobbyWindowController lobbyWindowController) {
        Main.lobbyWindowController = lobbyWindowController;
    }

    public static void setcWcontroller(CWcontroller cWcontroller) {
        Main.cWcontroller = cWcontroller;
    }

    public static void sendToChatWindow(String message) {
        Main.cWcontroller.displayReceivedMessage(message);
    }

    public static void exit() {
        ClientOutput.send(CommandsClientToServer.QUIT, "leaving now");
    }
}


