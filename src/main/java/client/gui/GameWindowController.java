package client.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tab;
import javafx.scene.control.Button;
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
import server.gamelogic.Dice;
import starter.Starter;

//import static client.gui.GameWindowHelper.entryNames;

/**
 * This is the Controller class for the Game window, i.e. the window in which the gameplay happens.
 * It implements Initializable.
 */
public class GameWindowController implements Initializable {
    private Logger logger = Starter.getLogger();

    @FXML
    private Button startButton;
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
    private ListView<DiceGUImplementation> diceBox;
    @FXML
    private ListView<DiceGUImplementation> diceBoxOther;
    @FXML
    private HBox hBoxEntries;
    @FXML
    private Button highScoreButton;
    @FXML
    private Button leaveGameButton;
    @FXML
    private Button leaveLobbyButton;
    @FXML
    private Label stealLabel;
    @FXML
    private Label freezeLabel;
    @FXML
    private Label deleteLabel;
    @FXML
    private Label swapLabel;
    @FXML
    private Label rotateLabel;

    @FXML
    private ListView<EntrySheetGUImplementation> entrySheet;

    @FXML
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

    private String ownerUser;

    int freezeCounter = 0;
    int swapCounter = 0;
    int stealCounter = 0;
    int shiftCounter = 0;
    int crossOutCounter = 0;

    int rollCounter = 0;


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
        usernameLabel.setText("username");

        //Initialize entry sheet
        entryList = GameWindowHelper.makeEntrySheet();

        entrySheet.setItems(entryList);

        //Initialize observable lists of dice
        //Main dice list
        diceList.addAll(new DiceGUImplementation[]{new DiceGUImplementation(0), new DiceGUImplementation(1), new DiceGUImplementation(2), new DiceGUImplementation(3), new DiceGUImplementation(4) });
        diceBox.setItems(diceList);

        //Dice list on tab 2
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

                    String scoreString = "";
                    if (entry.getScore() != -1) {
                        scoreString = String.valueOf(entry.getScore());
                    }
                    setText(title + separation + scoreString);
                    //setGraphic(imageView);
                }
            }
        });

        //initiate the arraylist that has all usernames of the players in the lobby
        ClientOutput.send(CommandsClientToServer.LOPL, "getting the players in the lobby");

        //createActionDiceListener();


        freezeLabel.setText("0");
        stealLabel.setText("0");
        swapLabel.setText("0");
        deleteLabel.setText("0");
        rotateLabel.setText("0");


        ClientOutput.send(CommandsClientToServer.RUSR, "");

    }

    /**
     * Updates the list of players in the lobby
     * @param playerList the list of players from the server, a String of the form "username,username,..."
     */
    public void updatePlayerList(String playerList) {
        String[] players = playerList.split(" ");
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
        swapButton.setDisable(true);
        deleteButton.setDisable(true);
        rollButton.setDisable(true);
        endTurnButton.setDisable(true);
        rollButton.setDisable(true);
        entryEnterButton.setDisable(true);

    }


    /**
     * Method enables all game fields except special action fields
     * can be used to enable button's when it is a player's turn
     */
    public void enableAllGameFields(){
        entrySheet.setDisable(false);
        diceBox.setDisable(false);
        rollButton.setDisable(false);
        endTurnButton.setDisable(false);
        freezeButton.setDisable(freezeLabel.getText().equals("0"));
        stealButton.setDisable(stealLabel.getText().equals("0"));
        deleteButton.setDisable(deleteLabel.getText().equals("0"));


    }


    /**
     * Method enables swap and shift actions if action dices available
     * can be used to enable button's when it is a player's turn in SwapAndShift phase
     */
    public void enableSwapAndShift () {
        swapButton.setDisable(swapLabel.getText().equals("0"));
        rotateButton.setDisable(rotateLabel.getText().equals("0"));
        endTurnButton.setDisable(false);
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
     * Mouse click on startButton
     */
    @FXML
    public void startGameAction(ActionEvent event) {
        ClientOutput.send(CommandsClientToServer.STRG, "lets start the game :)");
        logger.info("Game Start initialized by GUI");
        //Adds entry sheets of other players to second tab
        //Update list of players
        ClientOutput.send(CommandsClientToServer.LOPL, "getting the players in the lobby");
        logger.debug("List of Players in Lobby updated");
        leaveGameButton.setDisable(true);
    }

    /**
     * Starts a turn
     * @param userName
     * Player whose turn it is
     * @param phase
     * Main (normal round) or ShiftAndSwap (only shift and swap actions can be played)
     */
    public void initiateTurn(String userName, String phase) {
        for (DiceGUImplementation dice : diceList) {
            dice.resetDice();
        }
        //Clear information box before each turn
        clearInformationBox();
        displayInformationText("It is " + userName + "'s turn. May the power be with them.");
        switch (phase) {
            case "Main" -> displayInformationText("The phase is: " + phase + "\nThis is a normal round. SWAP and ROTATE actions cannot be played");
            case "ShiftSwap" -> displayInformationText("The phase is: " + phase + "\nIn this round only SWAP and ROTATE actions can be played.");

            default -> logger.debug("Invalid game phase received: " + phase);
        }
        if (userName.equals(ownerUser) && phase.equals("Main")) {
            enableAllGameFields();
        } else if (userName.equals(ownerUser) && phase.equals("ShiftSwap")) {
            enableSwapAndShift();
        } else {
            disableAllGameFields();
        }

        leaveGameButton.setDisable(true);
        leaveLobbyButton.setDisable(true);
        startButton.setDisable(true);
        entryEnterButton.setDisable(true);
        rollCounter = 0;
        logger.trace("rollCounter: " + rollCounter);
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

//     Will now be handled by enterEntryButton
//    /**
//     * Method that handles when an entry is selected (clicked on) in entry sheet
//     * @param event
//     */
//    @FXML
//    public void enterToEntrySheetAction(MouseEvent event) {
//        EntrySheetGUImplementation entry = entrySheet.getSelectionModel().getSelectedItem();
//        //send entry selection to gamelogic
//        ClientOutput.send(CommandsClientToServer.ENTY,  entry.getIDname());
//        entrySheet.refresh();
//    }


    /**
     * Method to open the high score list as a new window when pushing the highScoreButton
     */
    public void highScoreAction() {
        SceneController.showHighScoreWindow();
    }

    /**
     * Signals to server that player ends turn
     * @param event
     */
    @FXML
    public void endTurnAction(MouseEvent event) {
        //TODO: adapt message if necessary
        ClientOutput.send(CommandsClientToServer.ENDT,  "ended turn");
        informationBox.getChildren().clear();
        displayInformationText("You ended your turn ");

        diceBox.refresh();
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
     * Mouse click on rollButton
     */
    public void rollActionSend(ActionEvent event){
        String saveDiceString = GameWindowHelper.diceStashedArrToString(diceStashedList);
        //Saved dice are automatically transmitted before dice are rolled again
        //Count number of saved dice
        int diceSavedCounter = 0;
        logger.trace("diceSavedCountr: " + diceSavedCounter);
        for (DiceGUImplementation dice : diceList){
            if (dice.getSavingStatus()){
                diceSavedCounter++;
                logger.trace("diceSavedCountr: " + diceSavedCounter);

            }
        }

        if (diceSavedCounter < 5) {
            if (!saveDiceString.isEmpty()) {
                logger.info("The following dices are selected to be saved: " + saveDiceString);

                ClientOutput.send(CommandsClientToServer.SAVE, saveDiceString);

                //Set dice to save
                for (int i = 0; i < diceStashedList.length; i++) {
                    if (!diceStashedList[i].isEmpty()) {
                        diceList.get(i).setSavingStatus(true);
                        diceList.get(i).setStashStatus(false);
                        diceSavedCounter++;
                        logger.trace("diceSavedCountr: " + diceSavedCounter);

                    }
                    //Reset values in arrays with stashed dice
                    diceStashedList[i] = "";
                }
            } else {
                logger.info("No dices are selected to be saved.");

                ClientOutput.send(CommandsClientToServer.SAVE, "none");

            }
            //Pause for 100 ms
            try {
                logger.trace("Pause for 100 ms");
                TimeUnit.MILLISECONDS.sleep(100);
                logger.trace("Pause over");
            } catch (InterruptedException e) {
                logger.warn("Pause interrupted");
                throw new RuntimeException(e);
            }
        }
        if (rollCounter >= 2 || diceSavedCounter == 5) {
            rollButton.setDisable(true);
            entryEnterButton.setDisable(false);
        }

        if (rollCounter <= 2 ) {
            ClientOutput.send(CommandsClientToServer.ROLL, "");
            rollCounter++;
            logger.trace("rollCounter: " + rollCounter);
        }

        diceBox.refresh();
    }



    /**
     * Event handler for clicking on dice pictures in GUI, constructed via FXML API
     * if dice is clicked, the saving status of the dice is changed
     * the index of the dice is added (if not in array) or removed (if already in array) to the array of dice stashed for saving
     * @param event
     * Mouse click on dice
     */
    @FXML
    public void diceClick (MouseEvent event) {
        DiceGUImplementation dice = diceBox.getSelectionModel().getSelectedItem();
        if (!dice.getSavingStatus() && !dice.getStashStatus() && (dice.getDiceValue() != 0)) {
            diceStashedList[dice.getID()] = String.valueOf(dice.getID());
            dice.setStashStatus(true);
        } else if (!dice.getSavingStatus() && dice.getStashStatus() ) {
            diceStashedList[dice.getID()] = "";
            dice.setStashStatus(false);
        }
        logger.trace("Dice stashed for saving: " + Arrays.toString(diceStashedList));

        diceBox.refresh();
    }


    /**
     * Method to update values of dices in GUI
     * @param diceValues
     * Integer array of values (1-6) for 5 dices (usually provided by game logic engine)
     */
    public void receiveRoll( ObservableList<DiceGUImplementation> diceListToUpdate, int[] diceValues) {

        //index of dices in diceValues[]
        //int i = 0;
        for (int i=0; i < diceListToUpdate.size() && i < diceValues.length; i++) {
            diceListToUpdate.get(i).setDiceValue(diceValues[i]);
            //if (!dice.getSavingStatus() && i < diceValues.length){
                //dice.setDiceValue(diceValues[i]);
                //System.out.println(Arrays.toString(diceValues));
                //i++;
           // }
        }

        displayInformationText("ALEA IACTA EST! (the die is cast)");
        logger.info("send text to information window: ALEA IACTA EST");
        diceBox.refresh();
        diceBoxOther.refresh();

        //TODO: is that needed?
        //Save all dice on last roll
        if (rollCounter == 3){
            logger.trace("rollCounter: " + rollCounter);
            ClientOutput.send(CommandsClientToServer.SAVE, "0 1 2 3 4");

            for (DiceGUImplementation dice : diceList) {
                dice.setSavingStatus(true);
            }
        }

    }


    /**
     * Method to receive ArrayList &lt; String[] &gt;  of updated entries (arbitrary length) from GameLogic
     * and update ObservableList of primary EntrySheet
     * @param listOfEntries
     * ArrayList that contains entries as String arrays of size 2 with format {&lt; entry name &gt;, &lt; score &gt;}
     */
    public void receiveEntrySheet(ArrayList<String[]> listOfEntries) {
        for (String[] elem: listOfEntries) {
            //Check if string array has correct format: {&lt; entry name &gt;, &lt; score &gt;}
            if (elem != null && elem.length == 2 && entrySheetNameIndexMap.get(elem[0])!= null) {
                entryList.get(entrySheetNameIndexMap.get(elem[0])).setScore(Integer.parseInt(elem[1]));
            } else {
                logger.info("entry sheet cannot be updated due to invalid input format.");
            }
        }
        entrySheet.refresh();
    }

    /**
     * Method to receive ArrayList &lt; String[] &gt;  of updated entries (arbitrary length) from GameLogic
     * and update ObservableList of entry sheet of respective user on tab 2
     * @param userName
     * Name of user whose entry sheet is changed
     * @param listOfEntries
     * ArrayList of changed entries
     */
    public void updateEntrySheetTab2 (String userName, ArrayList<String[]> listOfEntries) {
        for (PlayerGUImplementation player : playersWithSheets){
            if ( player != null && player.getUsername().equals(userName) ) {
                for (String[] elem : listOfEntries) {
                    if (elem != null && elem.length == 2) {
                        if (player.getEntrySheet() != null && player.getEntrySheetListView() != null) {
                            player.getEntrySheet().get(entrySheetNameIndexMap.get(elem[0])).setScore(Integer.parseInt(elem[1]));
                            player.getEntrySheetListView().refresh();
                        }
                    } else{
                        logger.info("entry sheet cannot be updated due to invalid input format.");

                    }
                }

            }

        }
    }

    /**
     * Method to display text in information VBox of Game Window
     * @param informationText
     * String to be displayed in information box
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

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textFlow.getChildren().add(displayText);
                informationBox.getChildren().add(textFlow);
            }
        });

    }

    /**
     * Clears information box
     */
    public void clearInformationBox() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                informationBox.getChildren().clear();
            }
        });
    }




    /*
    Entry sheet controls
     */


    /**
     * Method to enter entry selection when entryEnterButton is clicked
     * 1. Gets selected field
     * 2. When selection is valid (field is still available) sends name of entry (e.g. "ones") to server
     * Mouse click of entryEnterButton
     */
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




    /**
     * Method to initialize second tab:
     * Iterates through playersInLobby list and creates for each player in list new instance of PlayerGUImplementation
     * 1. userName is set to the userName in playersInLobby list
     * 2. new ObservableList with entries is created
     * 3. new ListView representing entry sheet is created with ObservableList as items
     * 4. ListView is added to Hbox hBoxEntries on tab 2
     */
    public void initTabOther() {
        clearInformationBox();

        //Set username label
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                usernameLabel.setText(ownerUser);

            }
        });



        if (playersInLobby != null ) {
            for (int i = 0; i < playersInLobby.size() && i < playersWithSheets.length; i++) {
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
                            if (entry.getSavingStatus()) {
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

                String username = playersWithSheets[i].getUsername();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        VBox playerVBox = new VBox();
                        TextFlow playerTitle = new TextFlow();
                        playerTitle.getChildren().add(new Text(username));
                        playerVBox.getChildren().add(playerTitle);
                        playerVBox.getChildren().add(otherPlayerSheetListView);
                        hBoxEntries.getChildren().add(playerVBox);
                    }
                });



            }
        }
        displayInformationText(" \uD83C\uDFC1 LET THE GAME BEGIN \uD83C\uDFC1");
        logger.info("second tab initialized");

    }

    /**
     * Updates the action dice counter
     * @param actionDice
     */
    public void updateActionDice(String actionDice) {
        freezeCounter = 0;
        swapCounter = 0;
        stealCounter = 0;
        shiftCounter = 0;
        crossOutCounter = 0;

        String[] actions = actionDice.split("\\s+");

        for (String action : actions) {
            switch (action) {
                case "steal" -> stealCounter++;
                case "freeze" -> freezeCounter++;
                case "crossOut" -> crossOutCounter++;
                case "shift" -> shiftCounter++;
                case "swap" -> swapCounter++;
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                swapLabel.setText(Integer.toString(swapCounter));
                freezeLabel.setText(Integer.toString(freezeCounter));
                stealLabel.setText(Integer.toString(stealCounter));
                rotateLabel.setText(Integer.toString(shiftCounter));
                deleteLabel.setText(Integer.toString(crossOutCounter));
            }
        });



    }

    /**
     * Disables the action dice buttons if the counter is 0, enables the button if the counter is
     * positive.
     */
//    private void createActionDiceListener() {
////        swapLabel.textProperty().addListener(new ChangeListener<String>() {
////            @Override
////            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
////                swapButton.setDisable(swapLabel.getText().equals("0"));
////            }
////        });
//
//        freezeLabel.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                freezeButton.setDisable(freezeLabel.getText().equals("0"));
//            }
//        });
//
//        stealLabel.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                stealButton.setDisable(stealLabel.getText().equals("0"));
//            }
//        });
//
////        rotateLabel.textProperty().addListener(new ChangeListener<String>() {
////            @Override
////            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
////                rotateButton.setDisable(rotateLabel.getText().equals("0"));
////            }
////        });
//
//        deleteLabel.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                deleteButton.setDisable(deleteLabel.getText().equals("0"));
//            }
//        });
//
//
//
//    }

    /*
    Dice actions
     */
    public void shiftEntrySheets (String playerlist) {
        String[] playerArray = playerlist.split("\\s+");      // LINA DOM RI
        ArrayList<EntrySheetGUImplementation> sheetPlayer1TMP = null;
        ArrayList<EntrySheetGUImplementation> sheetPlayer2TMP = null;
        ArrayList<ArrayList<EntrySheetGUImplementation>> entrySheetsInOrder = new ArrayList<>();

        // iterate over player names
        for (int i = 0; i < playerArray.length; i++) {
            for (PlayerGUImplementation player : playersWithSheets) {
                if (player != null && player.getUsername().equals(playerArray[i])) {
                    entrySheetsInOrder.add(observableListToArrayList(player.getEntrySheet()));
                }
            }
        }

        for (int i = 0; i < entrySheetsInOrder.size(); i++) {
            PlayerGUImplementation player = playersWithSheets[i];
            if ( player !=null && player.getUsername().equals(playerArray[i])){
                int moduloIndex = (i+1) %  playersWithSheets.length;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        player.getEntrySheet().clear();
                        player.getEntrySheet().addAll(entrySheetsInOrder.get( moduloIndex));
                        //player.getEntrySheetListView().setItems(player.getEntrySheet());
                        player.getEntrySheetListView().refresh();
                    }
                });



        }
    }







    }


    public void swapEntrySheets(String userName1, String userName2){

        ArrayList<EntrySheetGUImplementation> temp1 = null;
        ArrayList<EntrySheetGUImplementation> temp2 = null;
        for (PlayerGUImplementation player : playersWithSheets) {
            if (player != null && player.getUsername().equals(userName1)) {

                temp1 = observableListToArrayList(player.getEntrySheet());
            }
            if (player != null && player.getUsername().equals(userName2)) {

                temp2 = observableListToArrayList(player.getEntrySheet());
            }
        }
        for (PlayerGUImplementation player : playersWithSheets) {
            if (player != null && player.getUsername().equals(userName1) && temp2 != null) {
                ArrayList<EntrySheetGUImplementation> finalTemp2 = temp2;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        player.getEntrySheet().clear();
                        player.getEntrySheet().addAll(finalTemp2);
                        //player.getEntrySheetListView().setItems(player.getEntrySheet());
                        player.getEntrySheetListView().refresh();
                    }
                });

            }
            if (player != null && player.getUsername().equals(userName2) && temp1 != null) {
                ArrayList<EntrySheetGUImplementation> finalTemp1 = temp1;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        player.getEntrySheet().clear();
                        player.getEntrySheet().addAll(finalTemp1);
                        //player.getEntrySheetListView().setItems(player.getEntrySheet());
                        player.getEntrySheetListView().refresh();
                    }
                });
            }
        }
    }

    private ArrayList<EntrySheetGUImplementation> observableListToArrayList (ObservableList<EntrySheetGUImplementation> observableEntries){
        ArrayList<EntrySheetGUImplementation> arrayListOfEntries = new ArrayList<>();
        arrayListOfEntries.addAll(observableEntries);
        return arrayListOfEntries;
    }

    public void setOwnUser(String ownUserName) {
        this.ownerUser = ownUserName;
    }

    public void endGame() {
        disableAllGameFields();
        leaveGameButton.setDisable(false);
        leaveLobbyButton.setDisable(false);
    }


}
