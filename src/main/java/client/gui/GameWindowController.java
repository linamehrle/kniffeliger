package client.gui;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import animatefx.animation.*;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.apache.logging.log4j.Logger;
import starter.Starter;


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
    private Label crossOutLabel;
    @FXML
    private Label swapLabel;
    @FXML
    private Label shiftLabel;

    @FXML
    private Label totalPointsLabel;

    @FXML
    private ListView<String> entrySheet = new ListView<>();

    @FXML
    private Button endTurnButton;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ToggleButton muteButton;
    @FXML
    private MediaView leverRoll;
    @FXML
    private Button infoButton;

    public ObservableList<DiceGUImplementation> diceList = FXCollections.observableArrayList();
    public ObservableList<DiceGUImplementation> diceListOther = FXCollections.observableArrayList();
    //variables for dice images
    private static Image[]  diceFaces = new Image[13];
    private static Image[]  numberImages = new Image[7];
    //List with dice selected for saving in GUI, but not yet saved
    private String[] diceStashedList = new String[]{"", "", "", "", ""};
    private ArrayList<String> playersInLobby;
    // MediaPlayer for background music of game window
    private MediaPlayer gameMainThemePlayer;
    private String ownerUser;

    /**
     * This map saves all the player names and their according entry sheets as observable lists
     */
    private HashMap<String, ObservableList<String>> playerToEntrySheetMap = new HashMap<>();

    /**
     * This List saves all list views used to display the entry sheets. The list views use the observable lists
     * form the playerToEntrySheetMap
     */
    private ArrayList<ListView<String>> secondTabListViews = new ArrayList<>();
    private AudioClip buttonSoundEffect1;
    private AudioClip buttonSoundEffect2;
    private MediaPlayer rollButtonAnimation;

    // Animations
    private FadeOutDownBig startButtonAnimationFade;
    private Flash startButtonAnimationFlash;
    private BounceIn highScoreButtonBounce;
    private FadeOutRightBig shiftAnimationFade;
    private RotateOutDownLeft swapAnimationFade;
    private Flash stealAnimationFlash;
    private Wobble freezeAnimationWobble;
    private Swing deleteAnimationSwing;



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
        // Font.loadFont(getClass().getResourceAsStream("/resources/fonts/SpaceAge.ttf"), 14);
        // logger.trace("Font loaded");

        //Set this instance of GameWindowController as controller in main
        Main.setGameWindowController(this);

        //set the username
        usernameLabel.setText("username");

        //Initialize observable lists of dice
        //Main dice list
        diceList.addAll(new DiceGUImplementation[] {new DiceGUImplementation(0), new DiceGUImplementation(1),
                new DiceGUImplementation(2), new DiceGUImplementation(3), new DiceGUImplementation(4)});
        diceBox.setItems(diceList);

        //Dice list on tab 2
        diceListOther.addAll(new DiceGUImplementation[] {new DiceGUImplementation(0), new DiceGUImplementation(1),
                new DiceGUImplementation(2), new DiceGUImplementation(3), new DiceGUImplementation(4)});
        diceBoxOther.setItems(diceListOther);

        //Load images
        GameWindowHelper.loadImagesToArray(diceFaces, "dice");
        GameWindowHelper.loadImagesToArray(numberImages, "number");
        ImageView number1 = new ImageView();
        number1.setImage(numberImages[1]);
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
                        baseIndex = 6;
                    }
                    ImageView imageView = new ImageView();
                    switch (dice.getDiceValue()) {
                        case 1 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 1]);
                        case 2 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 2]);
                        case 3 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 3]);
                        case 4 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 4]);
                        case 5 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 5]);
                        case 6 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 6]);
                        default -> imageView.setImage(GameWindowController.diceFaces[0]);
                    }
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
                        baseIndex = 6;
                    }
                    ImageView imageView = new ImageView();
                    switch (dice.getDiceValue()) {
                        case 1 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 1]);
                        case 2 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 2]);
                        case 3 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 3]);
                        case 4 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 4]);
                        case 5 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 5]);
                        case 6 -> imageView.setImage(GameWindowController.diceFaces[baseIndex + 6]);
                        default -> imageView.setImage(GameWindowController.diceFaces[0]);
                    }
                    setText(null);
                    setGraphic(imageView);
                }
            }
        });
        logger.info("Cell factory for DiceBox set");

        entrySheet.setCellFactory(param -> new ListCell<String>() {
            @Override
            public void updateItem(String entry, boolean empty) {
                super.updateItem(entry, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(entry);
                }
            }
        });

        //initiate the arraylist that has all usernames of the players in the lobby
        ClientOutput.send(CommandsClientToServer.LOPL, "getting the players in the lobby");

        disableAllGameFields();

        // Initialize animations
        startButtonAnimationFade = new FadeOutDownBig(startButton);
        // startButtonAnimationFade.setResetOnFinished(true);

        startButtonAnimationFlash = new Flash(startButton);
        startButtonAnimationFlash.setResetOnFinished(true);

        highScoreButtonBounce = new BounceIn(highScoreButton);
        highScoreButtonBounce.setResetOnFinished(true);

        shiftAnimationFade = new FadeOutRightBig(entrySheet);
        shiftAnimationFade.setResetOnFinished(true);

        swapAnimationFade = new RotateOutDownLeft(entrySheet);
        swapAnimationFade.setResetOnFinished(true);

        stealAnimationFlash = new Flash(entrySheet);
        stealAnimationFlash.setResetOnFinished(true);

        freezeAnimationWobble = new Wobble(entrySheet);
        freezeAnimationWobble.setResetOnFinished(true);

        deleteAnimationSwing = new Swing(entrySheet);
        deleteAnimationSwing.setResetOnFinished(true);


        // Load media / audio
        try {
            gameMainThemePlayer = GameWindowHelper.loadMedia("gameTheme.mp3");
            gameMainThemePlayer.setAutoPlay(true);
            gameMainThemePlayer.setCycleCount(MediaPlayer.INDEFINITE);
            gameMainThemePlayer.play();
            logger.trace("Audio file 'gameTheme.mp3' loaded.");
        } catch (FileNotFoundException e) {
            logger.info("Audio file 'gameTheme.mp3' not found.");
        }

        try {
            buttonSoundEffect1 = GameWindowHelper.loadSoundEffect("button1.mp3");
            buttonSoundEffect2 = GameWindowHelper.loadSoundEffect("soundEffect2.mp3");
            logger.trace("Sound effects loaded.");
        } catch (FileNotFoundException e) {
            logger.info("Sound effects not found.");
        }

        // Load Roll button animation
        try {
            rollButtonAnimation = GameWindowHelper.loadMedia("buttonRoll.mp4");
            logger.trace("Animation file 'buttonRoll.mp4' loaded.");
        } catch (FileNotFoundException e) {
            logger.info("Animation file 'buttonRoll.mp4' not found.");
        }

        leverRoll.setMediaPlayer(rollButtonAnimation);
        //rollButtonAnimation.setOnEndOfMedia(()->rollButton.setVisible(true));






        // Add listeners to sound controls
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number oldVal, Number newVal) {
                gameMainThemePlayer.setVolume(volumeSlider.getValue());
                buttonSoundEffect1.setVolume(volumeSlider.getValue());
                buttonSoundEffect2.setVolume(volumeSlider.getValue());
                if (volumeSlider.getValue() == 0 && !muteButton.isSelected()) {
                    muteButton.fire();
                } else if (volumeSlider.getValue() != 0 && muteButton.isSelected()) {
                    muteButton.fire();
                }
            }
        });

        ClientOutput.send(CommandsClientToServer.RUSR, "");
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
        swapButton.setDisable(true);
        deleteButton.setDisable(true);
        rollButton.setDisable(true);
        endTurnButton.setDisable(true);
        rollButton.setDisable(true);
        entryEnterButton.setDisable(true);
    }

    /**
     * Method to disable all action dice buttons
     */
    public void disableAllActionButtons() {
        stealButton.setDisable(true);
        freezeButton.setDisable(true);
        rotateButton.setDisable(true);
        swapButton.setDisable(true);
        deleteButton.setDisable(true);
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
        deleteButton.setDisable(crossOutLabel.getText().equals("0"));
    }


    /**
     * Method enables swap and shift actions if action dices available
     * can be used to enable button's when it is a player's turn in SwapAndShift phase
     */
    public void enableSwapAndShift () {
        swapButton.setDisable(swapLabel.getText().equals("0"));
        rotateButton.setDisable(shiftLabel.getText().equals("0"));
        endTurnButton.setDisable(false);
    }


    /**
     * Method that handles when the stealEntry Button is pressed to steal an entry from a player
     * @param event
     */
    public void stealEntryAction(ActionEvent event) {
        buttonSoundEffect1.play();
        stealAnimationFlash.play();
        SceneController.showActionPlayerAndFieldWindow(playersInLobby, "steal");
    }

    /**
     * Method that handles when the freezeEntry Button is pressed to freeze an entry from a player
     * @param event
     */
    public void freezeEntryButton(ActionEvent event) {
        buttonSoundEffect1.play();
        freezeAnimationWobble.play();
        SceneController.showActionPlayerAndFieldWindow(playersInLobby, "freeze");
    }

    /**
     * Method that handles when the rotateSheets Button is pressed to rotate the entry sheets of the players
     * @param event
     */
    public void rotateSheetsAction(ActionEvent event) {
        buttonSoundEffect1.play();
        shiftAnimationFade.play();
        ClientOutput.send(CommandsClientToServer.SHFT, "entry sheets shifted by one");
    }

    /**
     * Method that handles when the swapSheets Button is pressed to swap your entry sheet with one other player
     * @param event
     */
    public void swapSheetsAction(ActionEvent event) {
        buttonSoundEffect1.play();
        swapAnimationFade.play();
        SceneController.showActionPlayerWindow(playersInLobby);
    }

    /**
     * Method that handles when the deleteEntry Button is pressed to freeze one entry of another player
     * @param event
     */
    public void deleteEntryAction(ActionEvent event) {
        buttonSoundEffect1.play();
        deleteAnimationSwing.play();
        SceneController.showActionPlayerAndFieldWindow(playersInLobby, "delete");
    }

    /**
     * Method that handles when the startGame Button is pressed to start a game
     * @param event
     * Mouse click on startButton
     */
    @FXML
    public void startGameAction(ActionEvent event) {
        buttonSoundEffect1.play();
        startButtonAnimationFlash.play();
        ClientOutput.send(CommandsClientToServer.PREP, "prepare for game");
        logger.info("Game Start initialized by GUI");
    }

    @FXML
    public void infoButtonAction (ActionEvent event){

    }

    /**
     * This method prepares the gui for the game. It initiates the second tab.
     */
    public void initGame() {
        clearInformationBox();
        leaveGameButton.setDisable(true);
        disableAllActionButtons();
        initTabOther();
        buttonSoundEffect2.play();
        startButtonAnimationFade.play();
    }

    /**
     * Handles the mute action, i.e. mutes the music on demand
     * @param event
     */
    @FXML
    public void muteButtonAction(ActionEvent event) {
        if (muteButton.isSelected()){
            gameMainThemePlayer.pause();
            gameMainThemePlayer.setMute(true);
            volumeSlider.adjustValue(0.0);
            //muteButton.setText("Unmute");
        } else {
            gameMainThemePlayer.play();
            gameMainThemePlayer.setMute(false);
            volumeSlider.adjustValue(0.5);
            //muteButton.setText("Mute");
        }
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
        diceBox.refresh();

        //Clear information box before each turn
        clearInformationBox();

        if (userName.equals(ownerUser) && phase.equals("Main")) {
            enableAllGameFields();
            logger.debug("enabled all game fields");
        } else if (userName.equals(ownerUser) && phase.equals("ShiftSwap")) {
            enableSwapAndShift();
        } else {
            disableAllGameFields();
        }

        leaveGameButton.setDisable(true);
        leaveLobbyButton.setDisable(true);
        startButton.setDisable(true);
        entryEnterButton.setDisable(true);
    }

    /**
     * Method that handles when the leaveGame button is pressed to leave the game
     * @param event
     */
    public void leaveGameAction(ActionEvent event) {
        buttonSoundEffect1.play();
        Main.exit();
    }

    /**
     * Method to handle when the leaveLobby button is pressed to leave the current lobby
     * @param event
     */
    public void leaveLobbyAction(ActionEvent event) {
        buttonSoundEffect1.play();
        ClientOutput.send(CommandsClientToServer.LELO, "bye Lobby");

        //the scene switches again to the lobby window where the player can choose or create a new lobby
        SceneController.switchToLobbyWindow(event);
    }

    /**
     * Method to open the high score list as a new window when pushing the highScoreButton
     */
    public void highScoreAction() {
        buttonSoundEffect1.play();
        highScoreButtonBounce.play();
        SceneController.showHighScoreWindow();
    }

    /**
     * Signals to server that player ends turn
     * @param event
     */
    @FXML
    public void endTurnAction(MouseEvent event) {
        buttonSoundEffect1.play();
        ClientOutput.send(CommandsClientToServer.ENDT,  "ended turn");
        logger.debug("send end of turn to server");
        buttonSoundEffect2.play();
    }

    /**
     * Handles the end of a turn, the game buttons are disabled and dice reset
     */
    public void endTurn() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                informationBox.getChildren().clear();
                diceBox.refresh();
                endTurnButton.setDisable(true);
                disableAllGameFields();
            }
        });
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
        buttonSoundEffect1.play();
        rollButtonAnimation.seek(Duration.ZERO);
        rollButtonAnimation.play();
        String saveDiceString = GameWindowHelper.diceStashedArrToString(diceStashedList);

        logger.debug("Sending the dice to be saved: " + saveDiceString);

        if (!saveDiceString.isEmpty()) {
            logger.info("The following dices are selected to be saved: " + saveDiceString);

            ClientOutput.send(CommandsClientToServer.SAVE, saveDiceString);

            //Set dice to save
            for (int i = 0; i < diceStashedList.length; i++) {
                //Reset values in arrays with stashed dice
                diceStashedList[i] = "";
            }
        } else {
            logger.info("No dices are selected to be saved.");
            ClientOutput.send(CommandsClientToServer.SAVE, "none");
        }
        diceBox.refresh();
    }

    /**
     * Makes all dice red if the game logic saved the dice at the end of the third roll or send roll
     * to the game logic if there has not been a third roll
     * @param savedDice
     */
    public void handleSavedDice(String savedDice) {
        if (savedDice.equals("0 1 2 3 4 ")) {
            rollButton.setDisable(true);
            entryEnterButton.setDisable(false);
            for (DiceGUImplementation dice : diceList) {
                dice.setSavingStatus(true);
            }
            diceBox.refresh();
        } else {
            ClientOutput.send(CommandsClientToServer.ROLL, "");
        }
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
        buttonSoundEffect1.play();
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

        for (int i=0; i < diceListToUpdate.size() && i < diceValues.length; i++) {
            diceListToUpdate.get(i).setDiceValue(diceValues[i]);
        }
        buttonSoundEffect2.play();
        diceBox.refresh();
        diceBoxOther.refresh();
    }

    /**
     * Updates the listView for the entry sheet of a given player in the gui
     * @param entrySheetString the list of entries in the following form: "username:entry points, entry points,..."
     */
    public void updateEntrySheet(String entrySheetString) {
        String name = entrySheetString.split(":")[0];
        String entries = entrySheetString.split(":")[1];

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList<String> entryList = playerToEntrySheetMap.get(name);
                entryList.clear();
                entryList.addAll(entries.split(","));
            }
        });

        for (ListView<String> listView : secondTabListViews) {
            listView.refresh();
        }

        if (name.equals(ownerUser)) {
            entrySheet.setItems(playerToEntrySheetMap.get(ownerUser));
            entrySheet.refresh();
        }
        buttonSoundEffect2.play();
    }

    /**
     * Method to display text in information VBox of Game Window
     * @param informationText
     * String to be displayed in information box
     */

    public void displayInformationText(String informationText) {
        TextFlow textFlow = new TextFlow();
        //Background colour
        //textFlow.setStyle("-fx-background-color: rgb(233, 233, 235);");
        Text displayText = new Text(informationText);
        //Font
        //displayText.setFont(Font.font("Courier New"));

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
        buttonSoundEffect1.play();
        String entry = entrySheet.getSelectionModel().getSelectedItem();
        if (entry != null){
            ClientOutput.send(CommandsClientToServer.ENTY,  entry);
            logger.debug("Send entry " + entry + " to the client");
            //displayInformationText("You selected: " + entry);
        } else {
            displayInformationText("No entry field selected. Please select a valid entry field.");
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
        logger.info("initiating the second tab now");

        if (playersInLobby != null ) {
            for (String player : playersInLobby) {
                logger.debug("Second tab initiation: the player is " + player);
                ObservableList<String> otherPlayerEntryList = FXCollections.observableArrayList();
                ListView<String> otherPlayerSheetListView = new ListView<>();
                playerToEntrySheetMap.put(player, otherPlayerEntryList);
                otherPlayerSheetListView.setItems(otherPlayerEntryList);
                secondTabListViews.add(otherPlayerSheetListView);
                otherPlayerSheetListView.setCellFactory(param -> new ListCell<String>() {
                    @Override
                    public void updateItem(String entry, boolean empty) {
                        super.updateItem(entry, empty);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (empty) {
                                    setText(null);
                                    setGraphic(null);
                                } else {
                                    setText(entry);
                                    //setGraphic(imageView);
                                }
                            }
                        });
                    }
                });

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        VBox playerVBox = new VBox();
                        TextFlow playerTitle = new TextFlow();
                        playerTitle.getChildren().add(new Text(player));
                        playerVBox.getChildren().add(playerTitle);
                        playerVBox.getChildren().add(otherPlayerSheetListView);
                        hBoxEntries.getChildren().add(playerVBox);
                    }
                });
            }
        }

        logger.info("second tab initialized");
        ClientOutput.send(CommandsClientToServer.STRG, "lets start the game :)");
    }



    /**
     * Updates the action dice counter
     * @param actionDice A list of the action dices with count, looks the following way: "actionName:count,actionName:count,..."
     */
    public void updateActionDice(String actionDice) {

        logger.debug("updateActionDice in GameWindow: " + actionDice);

        //splits the whole list in the single action dices with count
        String[] splitActionDice = actionDice.split(",");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (String actionAndCount : splitActionDice) {
                    String action = actionAndCount.split(":")[0];
                    String count = actionAndCount.split(":")[1];

                    switch (action) {
                        case "SHIFT" -> shiftLabel.setText(count);
                        case "SWAP" -> swapLabel.setText(count);
                        case "FREEZE" -> freezeLabel.setText(count);
                        case "CROSSOUT" -> crossOutLabel.setText(count);
                        case "STEAL" -> stealLabel.setText(count);
                        default -> logger.info("update of the action dice did not work as planned");
                    }
                }
            }
        });

    }

    /**
     * Sets the own username for the gui
     * @param ownUserName
     */
    public void setOwnUser(String ownUserName) {
        this.ownerUser = ownUserName;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                usernameLabel.setText(ownerUser);
            }
        });

    }

    /**
     * Handles the end of the game, game buttons are disabled and the start, leave lobby and leave game buttons are enabled
     */
    public void endGame() {
        disableAllGameFields();
        leaveGameButton.setDisable(false);
        leaveLobbyButton.setDisable(false);
        startButton.setDisable(false);
    }


    /**
     * Method to mute (pause) background music
     */
    public void muteMainTheme() {
        gameMainThemePlayer.pause();
    }

    /**
     * This method updates the point score below the entry sheet to the new value
     * @param points
     */
    public void updateTotalPoints(String points) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                totalPointsLabel.setText(points);
            }
        });
    }

    /**
     * Visually shows that the given entry has been frozen.
     * @param entry
     */
    public void freezeEntry(String entry) {
        //TODO make the entry red or cross out or something?
    }

    /**
     * Resets the prior frozen entries back to the normal listView
     */
    public void defreezeEntrys() {
        //TODO reset the frozen entries, i.e. remove the cross out etc.
    }





}
