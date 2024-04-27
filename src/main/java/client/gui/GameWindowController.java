package client.gui;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tab;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.Logger;
import starter.Starter;

import static client.gui.GameWindowHelper.entryNames;

/**
 * This is the Controller class for the Game window, i.e. the window in which the gameplay happens.
 * It implements Initializable.
 */
public class GameWindowController implements Initializable {
    private Logger logger = Starter.getLogger();

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
    private VBox informationBox;

    @FXML
    private Label usernameLabel;
    @FXML
    private Tab ownerTab;
    @FXML
    private Tab otherPlayersTab;
    @FXML
    private Button entryEnterButton;
    @FXML
    private ListView<DiceGUImplementation> diceBoxOther;
    @FXML
    private HBox hBoxEntries;
    @FXML
    private Button highScoreButton;
    @FXML
    private ListView<EntrySheetGUImplementation> entrySheet;
    @FXML
    private ListView<DiceGUImplementation> diceBox;
    @FXML
    private ListView<DiceGUImplementation> getDiceBoxOther;
    @ FXML
    private Button endTurnButton;
    private ObservableList<EntrySheetGUImplementation> entryList = FXCollections.observableArrayList();
    public ObservableList<DiceGUImplementation> diceList = FXCollections.observableArrayList();
    public ObservableList<DiceGUImplementation> diceListOther = FXCollections.observableArrayList();
    //variables for dice images
    private static Image[]  diceFaces = new Image[13];
    //List with dice selected for saving in GUI, but not yet saved
    private String[] diceStashedList = new String[]{"", "", "", "", ""};
    //
    private HashMap<String, Integer> entrySheetNameIndexMap = GameWindowHelper.makeEntryToIntMap();
    private ArrayList<String> playersInLobby;
    private PlayerGUImplementation[] playersWithSheets = new PlayerGUImplementation[4];
    private ArrayList<ObservableList<EntrySheetGUImplementation>> playersSheets = new ArrayList<>();


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


        //Set this instance of GameWindowController as controller in main
        Main.setGameWindowController(this);

        //set the username
        usernameLabel.setText("username"); //TODO usernames of players for the sheets

        //Initialize entry sheet
        entryList = GameWindowHelper.makeEntrySheet();

        entrySheet.setItems(entryList);

        //Initialize observable list of dice
        diceList.addAll(new DiceGUImplementation[]{new DiceGUImplementation(0), new DiceGUImplementation(1), new DiceGUImplementation(2), new DiceGUImplementation(3), new DiceGUImplementation(4) });
        diceBox.setItems(diceList);

        diceListOther.addAll(new DiceGUImplementation[]{new DiceGUImplementation(0), new DiceGUImplementation(1), new DiceGUImplementation(2), new DiceGUImplementation(3), new DiceGUImplementation(4) });
        diceBoxOther.setItems(diceListOther);

        //Load images
        GameWindowHelper.loadImagesToArray(diceFaces);
        logger.info("Dice images loaded into GUI");


        //Cell factory for primary dice selection box
        diceBox.setCellFactory(param -> new ListCell<DiceGUImplementation>() {
            @Override
            public void updateItem(DiceGUImplementation dice, boolean empty) {
                super.updateItem(dice, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    //set baseIndex, such that different symbols are loaded for saved and unsaved dice
                    int baseIndex = 0;
                    if (dice.getSavingStatus() || dice.getStashStatus()) {
                        baseIndex = 6;}
                    ImageView imageView = new ImageView();
                    switch (dice.getDiceValue()) {
                        case 1 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 1]);
                        case 2 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 2]);
                        case 3 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 3]);
                        case 4 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 4]);
                        case 5 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 5]);
                        case 6 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 6]);
                        default -> imageView.setImage(GameWindowController.diceFaces[0]);
                    };

                    setText(null);
                    setGraphic(imageView);
                }
            }
        });

        //Set cell factory for dice box in tab 2
        diceBoxOther.setCellFactory(param -> new ListCell<DiceGUImplementation>() {
            @Override
            public void updateItem(DiceGUImplementation dice, boolean empty) {
                super.updateItem(dice, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    //set baseIndex, such that different symbols are loaded for saved and unsaved dice
                    int baseIndex = 0;
                    if (dice.getSavingStatus() || dice.getStashStatus()) {
                        baseIndex = 6;}
                    ImageView imageView = new ImageView();
                    switch (dice.getDiceValue()) {
                        case 1 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 1]);
                        case 2 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 2]);
                        case 3 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 3]);
                        case 4 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 4]);
                        case 5 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 5]);
                        case 6 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 6]);
                        default -> imageView.setImage(GameWindowController.diceFaces[0]);
                    };

                    setText(null);
                    setGraphic(imageView);
                }
            }
        });
        logger.info("Cell factory for DiceBox set");


        entrySheet.setCellFactory(param -> new ListCell<EntrySheetGUImplementation>() {
            @Override
            public void updateItem(EntrySheetGUImplementation entry, boolean empty) {
                super.updateItem(entry, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (entry.getSavingStatus() ) {
                        setDisable(true);
                    }
                    String title = entry.getIDname();
                    String separation = GameWindowHelper.fillWithTabulators(title, 4);

                    setText(title + separation + entry.getScore());
                    //setGraphic(imageView);
                }
            }
        });

        //initiate the arraylist that has all usernames of the players in the lobby
        ClientOutput.send(CommandsClientToServer.LOPL, "getting the players in the lobby");
    }

    /**
     * Updates the list of players in the lobby
     * @param playerList the list of players from the server, a String of the form "username,username,..."
     */
    public void updatePlayerList(String playerList) {
        String[] players = playerList.split(",");
        playersInLobby = new ArrayList<>();
        for (int i = 0; i < players.length; i++) {
            playersInLobby.add(players[i]);
        }
    }



    /*
    Methods for enabling/disabling buttons
     */

    /**
     * Method disables all game fields (but not leave and quit buttons)
     * can be used to disable fields when it is another player's turn
     */
    public void disableAllGameFields(){
        entrySheet.setDisable(true);
        diceBox.setDisable(true);
        stealButton.setDisable(true);
        freezeButton.setDisable(true);
        rotateButton.setDisable(true);
        rollButton.setDisable(true);
        swapButton.setDisable(true);
        deleteButton.setDisable(true);
        rollButton.setDisable(true);

    }

    /**
     * Method enables all game fields except special action fields
     * can be used to enable button's when it is a player's turn
     */
    public void enableAllGameFields(){
        entrySheet.setDisable(false);
        diceBox.setDisable(false);
        rollButton.setDisable(false);
    }


    /**
     * Method that handles when the stealEntry Button is pressed to steal an entry from a player
     * @param event
     */
    public void stealEntryAction(ActionEvent event) {
        SceneController.showActionPlayerAndFieldWindow(playersInLobby, "steal");
    }

    /**
     * Method that handles when the freezeEntry Button is pressed to freeze an entry from a player
     * @param event
     */
    public void freezeEntryButton(ActionEvent event) {
        SceneController.showActionPlayerAndFieldWindow(playersInLobby, "freeze");
    }

    /**
     * Method that handles when the rotateSheets Button is pressed to rotate the entry sheets of the players
     * @param event
     */
    public void rotateSheetsAction(ActionEvent event) {
        ClientOutput.send(CommandsClientToServer.SHFT, "entry sheets shifted by one");
    }

    /**
     * Method that handles when the swapSheets Button is pressed to swap your entry sheet with one other player
     * @param event
     */
    public void swapSheetsAction(ActionEvent event) {
        SceneController.showActionPlayerWindow(playersInLobby);
    }

    /**
     * Method that handles when the deleteEntry Button is pressed to freeze one entry of another player
     * @param event
     */
    public void deleteEntryAction(ActionEvent event) {
        SceneController.showActionPlayerAndFieldWindow(playersInLobby, "delete");
    }

    /**
     * Method that handles when the startGame Button is pressed to start a game
     * @param event
     */
    @FXML
    public void startGameAction(ActionEvent event) {
        ClientOutput.send(CommandsClientToServer.STRG, "lets start the game :)");
        logger.info("Game Start initialized by GUI");
        //Adds entry sheets of other players to second tab
        //Update list of players
        ClientOutput.send(CommandsClientToServer.LOPL, "getting the players in the lobby");
        logger.info("List of Players in Lobby updated");
        initTabOther();
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
     * Method that handles when an entry is selected (clicked on) in entry sheet
     * @param event
     */
    @FXML
    public void enterToEntrySheetAction(MouseEvent event) {
        EntrySheetGUImplementation entry = entrySheet.getSelectionModel().getSelectedItem();
        //send entry selection to gamelogic
        ClientOutput.send(CommandsClientToServer.ENTY,  entry.getIDname());
        entrySheet.refresh();
    }

    /**
     * Method that handles when the saveDice Button is pressed to save certain dice before re rolling
     * @param event
     */
    public void saveDiceAction(ActionEvent event) {
        //TODO
    }


    /**
     * Method to open the high score list as a new window when pushing the highScoreButton
     */
    public void highScoreAction() {
        SceneController.showHighScoreWindow();
    }

    @FXML
    public void endTurnAction(MouseEvent event) {
        //action
    }




    /**
     * Method to send roll command to server when rollButton is pressed
     * The following actions are performed:
     * diceStashedList (list of dice to be saved) is converted from String[] to String and sent to server/gamelogic
     * savingStatus of saved dice in GUI is set to true
     * stashStatus of dice is set to false
     * elements of diceStashedList is set to empty string
     * if dicedStashedList is empty, command 'none' is sent to server
     * roll command is sent to server
     * @param event
     */
    public void rollActionSend(ActionEvent event){
        String saveDiceString = GameWindowHelper.diceStashedArrToString(diceStashedList);
        //Saved dice are automatically transmitted before dice are rolled again
        if ( !saveDiceString.isEmpty()) {
            ClientOutput.send(CommandsClientToServer.SAVE,  saveDiceString);

            //Set dice to saved
            for (int i=0; i < diceStashedList.length; i++){
                if ( !diceStashedList[i].isEmpty() ){
                    diceList.get(i).setSavingStatus(true);
                    diceList.get(i).setStashStatus(false);
                }
                //Reset values in arrays with stashed dice
                diceStashedList[i] = "";
            }
        }
        else {
            ClientOutput.send(CommandsClientToServer.SAVE,  "none");
        }
        diceBox.refresh();
        ClientOutput.send(CommandsClientToServer.ROLL, "roll" );
    }



    /**
     * Event handler for clicking on dice pictures in GUI, constructed via FXML API
     * if dice is clicked, the saving status of the dice is changed
     * the index of the dice is added (if not in array) or removed (if already in array) to the array of dice stashed for saving
     * @param event
     */
    @FXML
    public void diceClick (MouseEvent event) {
        DiceGUImplementation dice = diceBox.getSelectionModel().getSelectedItem();
        if (!dice.getSavingStatus() && !dice.getStashStatus() && (dice.getDiceValue() != 0)) {
            //ClientOutput.send(CommandsClientToServer.GAME, String.valueOf(dice.getID()));
            diceStashedList[dice.getID() - 1] = String.valueOf(dice.getID());
            dice.setStashStatus(true);
        } else if (!dice.getSavingStatus() && dice.getStashStatus() ) {
            //ClientOutput.send(CommandsClientToServer.GAME, String.valueOf(dice.getID()));
            diceStashedList[dice.getID() - 1] = "";
            dice.setStashStatus(false);
        }
        diceBox.refresh();
    }


    /**
     * Method to update values of dices in GUI
     * @param diceValues integer array of values (1-6) for 5 dices (usually provided by game logic engine)
     */
    public void receiveRoll( ObservableList<DiceGUImplementation> diceListToUpdate, int[] diceValues) {
        int i = 0;
        for (DiceGUImplementation dice : diceListToUpdate) {
            if ( !dice.getSavingStatus() ) {
                dice.setDiceValue(diceValues[i]);
            }
            i++;
        }
        diceBox.refresh();
    }


    /**
     * Method to receive String of updated entries (arbitrary length) from GameLogic
     * @param listOfEntries ArrayList that contains entries as String arrays of size 2 with format {<entry name>, <score>}
     */
    public void receiveEntrySheet(ArrayList<String[]> listOfEntries) {
        for (String[] elem: listOfEntries) {
            //Check if string array has correct format: {<entry name>, <score>}
            if (elem.length == 2) {
                entryList.get(entrySheetNameIndexMap.get(elem[0])).setScore(Integer.parseInt(elem[1]));
            } else {
                logger.info("entry sheet cannot be updated due to invalid input format");
            }
        }
        entrySheet.refresh();
    }

    /**
     * Method to display text in information VBox of Game Window
     * @param informationText
     */
    //TODO: Move layout (font, colours) to CSS?
    public void displayInformationText(String informationText) {
        TextFlow textFlow = new TextFlow();
        //Background colour
        textFlow.setStyle(
                "-fx-background-color: rgb(233, 233, 235);");
        Text displayText = new Text(informationText);
        //Font
        displayText.setFont(Font.font("Courier New"));
        textFlow.getChildren().add(displayText);
        //Clear field first (alternative scrollplane with old information at the bottom?)
        informationBox.getChildren().clear();
        informationBox.getChildren().add(textFlow);
    }



    /*
    Entry sheet controls
     */
    public void entryClickAction(MouseEvent event){

    }

    @FXML
    public void entryEnterButtonAction(MouseEvent event){
        EntrySheetGUImplementation entry = entrySheet.getSelectionModel().getSelectedItem();
        if (entry != null && !entry.getSavingStatus()){
            String entryIDName = entry.getIDname();
            ClientOutput.send(CommandsClientToServer.ENTY,  entryIDName);
            displayInformationText("You selected: " + entryIDName);
        } else {
            displayInformationText("No valid entry field selected. Please select a valid entry field.");
        }

    }


    public void initTabOther() {
        //Clear HBox before adding players
        hBoxEntries.getChildren().clear();

        for (int i=0; i < playersInLobby.size() && i < playersWithSheets.length; i++){
            playersWithSheets[i] = new PlayerGUImplementation(playersInLobby.get(i));

            ListView<EntrySheetGUImplementation> otherPlayerSheetListView = new ListView<>();
            otherPlayerSheetListView.setItems(playersWithSheets[i].getEntrySheet());
            otherPlayerSheetListView.setCellFactory(param -> new ListCell<EntrySheetGUImplementation>() {
                @Override
                public void updateItem(EntrySheetGUImplementation entry, boolean empty) {
                    super.updateItem(entry, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        if (entry.getSavingStatus() ) {
                            setDisable(true);
                        }
                        String title = entry.getIDname();
                        String separation = GameWindowHelper.fillWithTabulators(title, 4);

                        setText(title + separation + entry.getScore());
                        //setGraphic(imageView);
                    }
                }
            });
            playersWithSheets[i].setEntrySheetListView(otherPlayerSheetListView);

            VBox playerVBox = new VBox();
            TextFlow playerTitle = new TextFlow();
            playerTitle.getChildren().add(new Text(playersWithSheets[i].getUsername()));
            playerVBox.getChildren().add(playerTitle);
            playerVBox.getChildren().add(otherPlayerSheetListView);
            hBoxEntries.getChildren().add(playerVBox);

        }

    }


}
