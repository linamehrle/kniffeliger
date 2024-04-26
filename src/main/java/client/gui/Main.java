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

import java.util.ArrayList;

/**
 * This is the main class for the gui, it handles the start and communication from the network to the gui.
 */
public class Main extends Application {
    private Logger logger = Starter.logger;

    private static LobbyWindowController lobbyWindowController;
    private static CWcontroller cWcontroller;

    private static GameWindowController gameWindowController;
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

    public static void setGameWindowController(GameWindowController controller) {
        Main.gameWindowController = controller;
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
     * Sends the current high score list to the gui to display
     * @param highScore
     */
    public static void updateHighScore(String highScore) {
        Main.highScoreController.updateHighScore(highScore);
    }

    /**
     * Method to relay dice values to GUI (Game Window Controller)
     * @param diceValues string of 5 dice values separated by empty spaces
     */
    public static void sendDiceToGUI(String diceValues) {
        int[] diceValueArray = parseIntArray(diceValues);
        Main.gameWindowController.receiveRoll(diceValueArray);
    }

    public static void sendEntrySheetToGUI (String entrySheetString) {
        //First split results at spaces giving entryFieldName:score
        String[] nameValueStrings = entrySheetString.split(" ");
        ArrayList<String[]> listOfEntries = new ArrayList<>();
        for (String elem : nameValueStrings){
            //Split at : giving arrays containing {entryFieldName, score}
            String[] nameValuePairs = elem.split(":");
            listOfEntries.add(nameValuePairs);
        }
        Main.gameWindowController.receiveEntrySheet(listOfEntries);
    }

    public static void sendInformationTextToGUI(String informationText) {
        Main.gameWindowController.displayInformationText(informationText);
    }

    /**
     * Sends a new lobby status to the gui to update the list
     * @param lobby
     */
    public static void updateLobby(String lobby) {
        Main.lobbyWindowController.updateLobbyStatus(lobby);
    }

    //Put this in a separate file with helper functions?
    /**
     * Hekper function to convert dice values received as string to array
     * @param input
     * @return
     */
    public static int[] parseIntArray(String input) {
        String[] numbers = input.split(" ");
        int[] array = new int[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            array[i] = Integer.parseInt(numbers[i]);
        }

        return array;
    }

    public static void updateChatPlayerList(String playerList) {
        if (cWcontroller != null) {
            Main.cWcontroller.updatePlayerList(playerList);
        }
    }

    /**
     * Communicates to the server that a player wants to leave the game
     */
    public static void exit() {
        ClientOutput.send(CommandsClientToServer.QUIT, "leaving now");
    }

}


