package client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;
import org.apache.logging.log4j.Logger;
import starter.Starter;

/**
 * This is the main class for the gui, it handles the start and communication from the network to the gui.
 */
public class Main extends Application {
    private Logger logger = Starter.logger;

    private static LobbyWindowController lobbyWindowController;
    private static CWcontroller cWcontroller;
    private static HighScoreController highScoreController;
    Stage mainWidow;
    Stage chatWindow;

    /**
     * The start method for the gui
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage) {
        mainWidow = stage;
        SceneController.setMainWindow(mainWidow);
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/LobbyWindow.fxml"));
            Parent root = loader.load();

            mainWidow.setOnCloseRequest(e -> {
                //e.consume();
                exit();
            });
            mainWidow.setScene(new Scene(root, 600, 400));
            mainWidow.show();
            logger.info("Lobby Window started");

            //chatWindow = new Stage();
            //FXMLLoader loaderChat = new FXMLLoader(getClass().getResource("/chatwindow.fxml"));
            //Parent rootChat = (Parent)loaderChat.load();
            //Scene scene = new Scene(rootChat, 600, 300, Color.BLACK);
            //this.chatWindow.setScene(scene);
            //this.chatWindow.show();
            logger.info("Chat Window started");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Gives a new Lobby to the lobby window to add in the list
     * @param name
     */
    public static void addNewLobby(String name) {
        lobbyWindowController.addLobby(name);
    }

    /**
     * Gives a new Player to the lobby window to add to the list
     * @param lobbyAndPlayer
     */
    public static void addNewPlayer(String lobbyAndPlayer) {
        lobbyWindowController.addPlayerToLobby(lobbyAndPlayer);
    }

    /**
     * gives the whole list of lobbies and players to the gui to initialize the list
     * @param lobbies
     */
    public static void lobbyList(String lobbies) {
        lobbyWindowController.initializeLobbyList(lobbies);
    }

    /**
     * Gives a player to the lobby window to remove from the list
     * @param lobbyAndPlayerName
     */
    public static void removePlayer(String lobbyAndPlayerName) {
        lobbyWindowController.removePlayerFromList(lobbyAndPlayerName);
    }

    /**
     * Setter for the lobbyWindowController
     * @param lobbyWindowController
     */
    public static void setLobbyWindowController(LobbyWindowController lobbyWindowController) {
        Main.lobbyWindowController = lobbyWindowController;
    }

    /**
     * Setter for the cWcontroller
     * @param cWcontroller
     */
    public static void setcWcontroller(CWcontroller cWcontroller) {
        Main.cWcontroller = cWcontroller;
    }

    /**
     * Setter for the highScoreController
     * @param highScoreController
     */
    public static void setHighScoreController(HighScoreController highScoreController) {
        Main.highScoreController = highScoreController;
    }

    /**
     * Sends a message to the chat window to display
     * @param message
     */
    public static void sendToChatWindow(String message) {
        Main.cWcontroller.displayReceivedMessage(message);
    }

    /**
     * Communicates to the server that a player wants to leave the game
     */
    public static void exit() {
        ClientOutput.send(CommandsClientToServer.QUIT, "leaving now");
    }
}


