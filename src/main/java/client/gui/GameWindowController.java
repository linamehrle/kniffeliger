package client.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;
import org.apache.logging.log4j.Logger;
import starter.Starter;

/**
 * This is the Controller class for the Game window, i.e. the window in which the gameplay happens.
 * It implements Initializable.
 */
public class GameWindowController implements Initializable {
    private Logger logger = Starter.logger;

    @FXML
    private Button stealButton;
    @FXML
    private Button freezeButton;
    @FXML
    private Button rotateButton;
    @FXML
    private Button swapButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button rollButton;
    @FXML
    private Button enterButton;

    @FXML
    TextField diceTextField;

    @FXML
    Button saveDiceButton;

    @FXML
    private ListView<String> entrySheet;
    @FXML
    private ListView<String> dice;

    private static ObservableList<String> entryList = FXCollections.observableArrayList();
    private static ObservableList<String> diceList = FXCollections.observableArrayList();

    /**
     * The initialize function for the Game Window, it sets everything up.
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Game Window initialized");

        //einträge im entry sheet
        entryList.addAll("ones", "twos", "threes", "fours", "fives", "sixes",
                "threeOfAKind", "fourOfAKind", "fullHouse", "smallStraight", "largeStraight",
                "kniffeliger", "chance", "pi");
        entrySheet.setItems(entryList);

        //würfel am anfang auf null
        diceList.addAll("0", "0", "0", "0", "0");
        dice.setItems(diceList);

        entrySheet.setDisable(true);
        dice.setDisable(true);
        //diceTextField.setDisable(true);

        stealButton.setDisable(true);
        freezeButton.setDisable(true);
        rotateButton.setDisable(true);
        rollButton.setDisable(true);
        swapButton.setDisable(true);
        deleteButton.setDisable(true);
        rollButton.setDisable(true);
        enterButton.setDisable(true);
        saveDiceButton.setDisable(true);

        //Listener to see if the text field to save the dice is active
        diceTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(diceTextField.isFocused()){
                    saveDiceButton.setDisable(false);
                }
            }
        });

    }

    /**
     * Method that handles when the stealEntry Button is pressed to steal an entry from a player
     * @param event
     */
    public void stealEntryAction(ActionEvent event) {
        //TODO
    }

    /**
     * Method that handles when the freezeEntry Button is pressed to freeze an entry from a player
     * @param event
     */
    public void freezeEntryButton(ActionEvent event) {
        //TODO
    }

    /**
     * Method that handles when the rotateSheets Button is pressed to rotate the entry sheets of the players
     * @param event
     */
    public void rotateSheetsAction(ActionEvent event) {
        //TODO
    }

    /**
     * Method that handles when the swapSheets Button is pressed to swap your entry sheet with one other player
     * @param event
     */
    public void swapSheetsAction(ActionEvent event) {
        //TODO
    }

    /**
     * Method that handles when the deleteEntry Button is pressed to freeze one entry of another player
     * @param event
     */
    public void deleteEntryAction(ActionEvent event) {
        //TODO
    }

    /**
     * Method that handles when the startGame Button is pressen to start a game
     * @param event
     */
    public void startGameAction(ActionEvent event) {
        ClientOutput.send(CommandsClientToServer.STRT, "lets start the game :)");
        logger.info("Game Start initialized by GUI");
    }

    /**
     * Method that handles when the leaveGame button is pressed to leave the game
     * @param event
     */
    public void leaveGameAction(ActionEvent event) {
        Main.exit();
    }

    /**
     * Method to handle when the leaveLobby button is pressed to leave the current lobby
     * @param event
     */
    public void leaveLobbyAction(ActionEvent event) {
        ClientOutput.send(CommandsClientToServer.LELO, "bye Lobby");

        //the scene switches again to the lobby window where the player can choose or create a new lobby
        SceneController.switchToLobbyWindow(event);
    }

    /**
     * Method that handles when the enterToEntrySheet Button is pressen to enter your dice into your entry sheet
     * @param event
     */
    public void enterToEntrySheetAction(ActionEvent event) {
        //TODO
    }

    /**
     * Method that handles when the saveDice Button is pressen to save certain dice before re rolling
     * @param event
     */
    public void saveDiceAction(ActionEvent event) {
        //TODO
    }
}
