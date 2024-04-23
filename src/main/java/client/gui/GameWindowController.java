package client.gui;

import java.awt.*;
import java.awt.Label;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private Button highScoreButton;

    @FXML
    TextField diceTextField;

    @FXML
    Button saveDiceButton;

    @FXML
    private ListView<EntrySheetGUImplementation> entrySheet;

    @FXML
    private ListView<DiceGUImplementation> diceBox;
    @FXML
    private VBox diceBox1;
    @FXML
    private VBox diceBox2;
    @FXML
    private VBox diceBox3;
    @FXML
    private VBox diceBox4;
    @FXML
    private VBox diceBox5;


    private static ObservableList<EntrySheetGUImplementation> entryList = FXCollections.observableArrayList();
    private ObservableList<DiceGUImplementation> diceList = FXCollections.observableArrayList();
    //variables for dice images
    private static Image[]  diceFaces = new Image[13];
    //List with dice selected for saving in GUI, but not yet saved
    private String[] diceStashedList = new String[]{"", "", "", "", ""};



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

        //Initialize entry sheet
        String[] entryNames = {"ones", "twos", "threes", "fours", "fives", "sixes",
                "threeOfAKind", "fourOfAKind", "fullHouse", "smallStraight", "largeStraight",
                "kniffeliger", "chance", "pi"};

        EntrySheetGUImplementation[] entryElements = new EntrySheetGUImplementation[entryNames.length];

        int k = 0;
        for (String name : entryNames){
            //Begin ID number of entries at 1, such that ones = 1, twos = 2 etc.
            entryElements[k] = new EntrySheetGUImplementation(k+1, name);
            k++;
        }

        entryList.addAll(entryElements);
        entrySheet.setItems(entryList);





        //Initialize observable list of dice
        diceList.addAll(new DiceGUImplementation[]{new DiceGUImplementation(1), new DiceGUImplementation(2), new DiceGUImplementation(3), new DiceGUImplementation(4), new DiceGUImplementation(5) });
        diceBox.setItems(diceList);



        //Listener to see if the text field to save the dice is active
        diceTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(diceTextField.isFocused()){
                    saveDiceButton.setDisable(false);
                }
            }
        });

        //Listener for diceBox ListView (might not be needed)
        diceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DiceGUImplementation>() {

            @Override
            public void changed(ObservableValue<? extends DiceGUImplementation> observable, DiceGUImplementation oldValue, DiceGUImplementation newValue) {
                //action
            }
        });



        //Load images
        IntStream.range(0, diceFaces.length).forEach(i -> {
            try {
                diceFaces[i] = diceImageLoader(i );
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        logger.info("Dice images loaded into GUI");


        //cell factory for diceBox selection box
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

        //Set Cell Factory for entrysheet
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
                    setText(entry.getIDname() + " " + entry.getScore());
                    //setGraphic(imageView);
                }
            }
        });


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
        diceTextField.setDisable(true);
        stealButton.setDisable(true);
        freezeButton.setDisable(true);
        rotateButton.setDisable(true);
        rollButton.setDisable(true);
        swapButton.setDisable(true);
        deleteButton.setDisable(true);
        rollButton.setDisable(true);
        enterButton.setDisable(true);
        saveDiceButton.setDisable(true);
    }

    /**
     * Method enables all game fields except special action fields
     * can be used to enable button's when it is a player's turn
     */
    public void enableAllGameFields(){
        entrySheet.setDisable(false);
        diceBox.setDisable(false);
        rollButton.setDisable(false);
        enterButton.setDisable(false);
        saveDiceButton.setDisable(false);

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
     * Method that handles when the startGame Button is pressed to start a game
     * @param event
     */
    @FXML
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

     /*
        Dice controls
     */

    public void highScoreAction() {
        //TODO
    }

    /**
     * Method to send roll command to server when rollButton is pressed
     * @param event
     */
    public void rollActionSend(ActionEvent event){
        String saveDiceString = diceStashedArrToString();
        //Saved dice are automatically transmitted before dice are rolled again
        if ( !saveDiceString.isEmpty()) {
            ClientOutput.send(CommandsClientToServer.GAME,  saveDiceString);

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
        diceBox.refresh();
        ClientOutput.send(CommandsClientToServer.GAME, "roll" );
    }

    /**
     * method to load images of dice faces
     * @param diceNumber number of dice face e.g. 1-6
     * @return ImageView object of diceFace
     * @throws FileNotFoundException
     */
    public static Image diceImageLoader(int diceNumber) throws FileNotFoundException {
        //String.valueOf(GameWindowController.class.getResource("/images/dice-" + diceNumber  + ".png"))
        //FileInputStream fis = new FileInputStream(file);
        String saved = "";
        if (diceNumber > 6){
            saved = "s";
            diceNumber = diceNumber -6;
        }
        return new Image(String.valueOf(GameWindowController.class.getResource("/images/dice-" + saved + diceNumber  + ".png")), 64, 63.2, true, false);
    }

    public void setDiceImage(HBox diceBoxID, int diceValue) {
        ImageView imageView = new ImageView();
        switch (diceValue) {
            case 1 -> imageView.setImage(GameWindowController.diceFaces[1]);
            case 2 -> imageView.setImage(GameWindowController.diceFaces[2]);
            case 3 -> imageView.setImage(GameWindowController.diceFaces[3]);
            case 4 -> imageView.setImage(GameWindowController.diceFaces[4]);
            case 5 -> imageView.setImage(GameWindowController.diceFaces[5]);
            case 6 -> imageView.setImage(GameWindowController.diceFaces[6]);
            default -> imageView.setImage(GameWindowController.diceFaces[0]);
        };

    }

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

    //This method is only necessary if surplus spaces are not ignored by gamelogic
    public String diceStashedArrToString(){
        StringBuilder saveMsgString = new StringBuilder();
        for (String elem:diceStashedList){
            if (! elem.isEmpty() ){
                saveMsgString.append(elem).append(" ");
            }
        }
        return saveMsgString.toString();
    }


    /**
     * Method to update values of dices in GUI
     * @param diceValues integer array of values (1-6) for 5 dices (usually provided by game logic engine)
     */
    public void receiveRoll(int[] diceValues) {
        int i = 0;
        for (DiceGUImplementation dice : this.diceList) {
            //TODO: add null-check
            if ( !dice.getSavingStatus() ) {
                dice.setDiceValue(diceValues[i]);
            }
            i++;
        }
    }
}
