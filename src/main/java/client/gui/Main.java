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
    private static Logger logger = Starter.getLogger();

    private static LobbyWindowController lobbyWindowController;
    private static CWcontroller cWcontroller;
    private static GameWindowController gameWindowController;
    private static HighScoreController highScoreController;

    private Stage mainWidow;



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
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/TrailerWindow.fxml"));
            Parent root = loader.load();

            mainWidow.setOnCloseRequest(e -> {
                //e.consume();
                exit();
            });
            mainWidow.setScene(new Scene(root));
            mainWidow.show();
            //logger.info("Lobby Window started");

            //logger.info("Chat Window started");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Gives a new Lobby to the lobby window to add in the list
     * @param name
     */
    public static void addNewLobby(String name) {
        if (lobbyWindowController != null) {
            lobbyWindowController.addLobby(name);
        }
    }

    /**
     * Gives a new Player to the lobby window to add to the list
     * @param lobbyAndPlayer
     */
    public static void addNewPlayer(String lobbyAndPlayer) {
        if (lobbyWindowController != null) {
            lobbyWindowController.addPlayerToLobby(lobbyAndPlayer);
        }
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
     * Getter for the lobby window controller
     * @return
     */
    public static LobbyWindowController getLobbyWindowController() {
        return lobbyWindowController;
    }

    /**
     * Setter for the game window controller
     * @param controller
     */
    public static void setGameWindowController(GameWindowController controller) {
        Main.gameWindowController = controller;
    }

    /**
     * Getter for the game window controller
     * @return
     */
    public static GameWindowController getGameWindowController() {
        return gameWindowController;
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
        logger.info("Main: dice received from client input");
        int[] diceValueArray = parseIntArray(diceValues);
        gameWindowController.receiveRoll(gameWindowController.diceList, diceValueArray);
    }

    /**
     * Sends a list of dice to the game window to update in the second tab
     * @param diceValues
     */
    public static void updateOtherDiceBox(String diceValues){
        int[] diceValueArray = parseIntArray(diceValues);
        Main.gameWindowController.receiveRoll(gameWindowController.diceListOther, diceValueArray);
    }

    /**
     * Send a new entry sheet to the game window to dispaly
     * @param entrySheetString
     */
    public static void updateEntrySheet (String entrySheetString) {
        gameWindowController.updateEntrySheet(entrySheetString);
    }

    /**
     * Sends a new lobby status to the gui to update the list
     * @param lobby
     */
    public static void updateLobby(String lobby) {
        Main.lobbyWindowController.updateLobbyStatus(lobby);
    }

    /**
     * Helper function to convert dice values received as string to array
     * @param input
     * @return
     */
    public static int[] parseIntArray(String input) {
        String[] numbers = input.split(" ");
        int[] array = new int[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            if (!numbers[i].isEmpty()) {
                array[i] = Integer.parseInt(numbers[i]);
            }
        }

        return array;
    }

    /**
     * Used to update the player list in the drop-down of the chat window
     * @param playerList
     */
    public static void updateChatPlayerList(String playerList) {
        if (cWcontroller != null) {
            Main.cWcontroller.updatePlayerList(playerList);
        }
    }

    /**
     * Used to update the list of players in the lobby in the game window controller
     * @param playerList
     */
    public static void updateGamePlayerList(String playerList) {
        if (gameWindowController != null) {
            Main.gameWindowController.updatePlayerList(playerList);
        }
    }

    /**
     * Sends the current action dice to the game window to update the counter
     * @param actionDice
     */
    public static void updateActionDice(String actionDice) {
        Main.gameWindowController.updateActionDice(actionDice);
    }

    /**
     * Gives a string to the game window to display in the information field
     * @param text
     */
    public static void displayInGameWindow(String text) {
        if (gameWindowController != null) {
            gameWindowController.displayInformationText(text);
        }
    }

    /**
     * Communicates to the gui who's turn it is and which turn it is
     * @param usernameAndPhase
     */
    public static void changeTurn(String usernameAndPhase) {
        String[] userPhaseSplit = usernameAndPhase.split(" ");

        if (userPhaseSplit.length >= 2) {
            gameWindowController.initiateTurn(userPhaseSplit[0], userPhaseSplit[1]);
        }

    }

    /**
     * Method to relay to the GUI that Tab 2 has to be initialized with entry sheets of other players
     */
    public static void initGame() {
        gameWindowController.initGame();
    }

    /**
     * Communicates to the server that a player wants to leave the game
     */
    public static void exit() {
        ClientOutput.send(CommandsClientToServer.QUIT, "leaving now");
    }

    /**
     * Communicates the own username to the gui
     * @param ownUserName
     */
    public static void sendOwnNameToGUI(String ownUserName){
        gameWindowController.setOwnUser(ownUserName);

    }

    /**
     * Communicated the end of the game to the game window
     */
    public static void sendEndOfGame() {
        gameWindowController.endGame();
    }

    /**
     * Sends the updated score to the game window
     * @param points
     */
    public static void updateTotalScore(String points) {
        gameWindowController.updateTotalPoints(points);
    }

    /**
     * Gives a freeze or defreeze command to the game window to cross out an entry or return back to normal
     * @param freezeInformation this string is either "defreeze" or "freeze:entry"
     */
    public static void freezeOrDefreeze(String freezeInformation) {
        if (freezeInformation.equals("defreeze")) {
            gameWindowController.defreezeEntrys();
        } else {
            gameWindowController.freezeEntry(freezeInformation.split(":")[1]);
        }
    }

    /**
     * Communicates the saved dice to the game window
     * @param savedDice
     */
    public static void communicateSave(String savedDice) {
        gameWindowController.handleSavedDice(savedDice);
    }

    /**
     * Communicates the end of turn to the game window
     */
    public static void endTurn() {
        gameWindowController.endTurn();
    }
}


