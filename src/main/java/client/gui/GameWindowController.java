package client.gui;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
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
    private VBox informationBox;

    @FXML
    private Label usernameLabel;


    @FXML
    private Button highScoreButton;



//    @FXML
//    private TableView<EntrySheetGUImplementation> entrySheet;
//    //Score column of entry sheet
//    @FXML
//    private TableColumn<EntrySheetGUImplementation, Integer> entrySheetScores;
//    @FXML
//    private TableColumn<EntrySheetGUImplementation, String> entrySheetNames;
//    @FXML
//    private TableColumn<EntrySheetGUImplementation, String> entrySheetIcons;
    @FXML
    private ListView<EntrySheetGUImplementation> entrySheet;

    @FXML
    private ListView<DiceGUImplementation> diceBox;
    @ FXML
    private Button endTurnButton;
    private ObservableList<EntrySheetGUImplementation> entryList = FXCollections.observableArrayList();
    private ObservableList<DiceGUImplementation> diceList = FXCollections.observableArrayList();
    //variables for dice images
    private static Image[]  diceFaces = new Image[13];
    //List with dice selected for saving in GUI, but not yet saved
    private String[] diceStashedList = new String[]{"", "", "", "", ""};
    //
    private HashMap<String, Integer> entrySheetNameIndexMap = new HashMap<>();




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
        EntrySheetGUImplementation[] entryElements;
        entryElements = makeEntrySheetElements();


        entryList.addAll(entryElements);

//        entrySheetNames.setCellValueFactory(cellData -> cellData.getValue().nameProperty()); // new PropertyValueFactory<>("name"));
//        entrySheetIcons.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//        entrySheetScores.setCellValueFactory(cellData -> (cellData.getValue().scoreProperty()).asObject());

        entrySheet.setItems(entryList);



        //Initialize observable list of dice
        diceList.addAll(new DiceGUImplementation[]{new DiceGUImplementation(1), new DiceGUImplementation(2), new DiceGUImplementation(3), new DiceGUImplementation(4), new DiceGUImplementation(5) });
        diceBox.setItems(diceList);



        //Listener to see if the text field to save the dice is active
//        diceTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                if(diceTextField.isFocused()){
//                    saveDiceButton.setDisable(false);
//                }
//            }
//        });

        //Listener for diceBox ListView (might not be needed)
//        diceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DiceGUImplementation>() {
//
//            @Override
//            public void changed(ObservableValue<? extends DiceGUImplementation> observable, DiceGUImplementation oldValue, DiceGUImplementation newValue) {
//                //action
//            }
//        });



        //Load images
        loadImagesToArray(diceFaces);
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
        logger.info("Cell factory for DiceBox set");


//        entrySheetNames.setCellFactory((tableColumn) -> new TableCell<>() {
//            @Override
//            protected void updateItem(String entry, boolean empty) {
//                super.updateItem(entry, empty);
//                if (empty) {
//                    super.setText(null);
//                } else {
//                    super.setText(entry);
//                }
//            }
//        });
//
//        entrySheetIcons.setCellFactory((tableColumn) -> new TableCell<>() {
//            @Override
//            protected void updateItem(String entry, boolean empty) {
//                super.updateItem(entry, empty);
//                if (empty) {
//                    super.setText(null);
//                } else {
//                    super.setText(entry);
//                }
//            }
//        });
//
//        entrySheetScores.setCellFactory((tableColumn) -> new TableCell<>() {
//            @Override
//            protected void updateItem(Integer entry, boolean empty) {
//                super.updateItem(entry, empty);
//                if (empty) {
//                    super.setText(null);
//                } else {
//                    super.setText(entry.toString());
//                }
//            }
//        });


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
                    String separation = fillWithTabulators(title, 4);

                    setText(title + separation + entry.getScore());
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
     * Method that handles when an entry is selected (clicked on) in entry sheet
     * @param event
     */
    @FXML
    public void enterToEntrySheetAction(MouseEvent event) {
        EntrySheetGUImplementation entry = entrySheet.getSelectionModel().getSelectedItem();
        //send entry selection to gamelogic
        ClientOutput.send(CommandsClientToServer.GAME,  entry.getIDname());
        entrySheet.refresh();
    }

    /**
     * Method that handles when the saveDice Button is pressed to save certain dice before re rolling
     * @param event
     */
    public void saveDiceAction(ActionEvent event) {
        //TODO
    }

     /*
        Dice controls
     */

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
        String saveDiceString = diceStashedArrToString(diceStashedList);
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
        else {
            ClientOutput.send(CommandsClientToServer.GAME,  "none");
        }
        diceBox.refresh();
        ClientOutput.send(CommandsClientToServer.GAME, "roll" );
    }


    /**
     * Loads dice images to Image array, such that images have only to be loaded once
     * @param imageArray Image array, to which the images are loaded
     */
    public static void loadImagesToArray(Image[] imageArray){
        IntStream.range(0, imageArray.length).forEach(i -> {
            try {
                imageArray[i] = diceImageLoader(i);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
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
     * Method to convert array of String containing indices of saved dices to String suitable to send to gamelogic
     * @param diceStashedList Array of String containing indices of dices to save as String
     * @return New string containing the indices of the dice separated by spaces
     */
    public static String diceStashedArrToString(String[] diceStashedList){
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
        diceBox.refresh();
    }

    //TODO: maybe add exception handling for null pointers and invalid strings
    public void receiveEntrySheet(ArrayList<String[]> entryElementList) {
        int i = 0;
        for (String[] elem: entryElementList) {
            entryList.get(entrySheetNameIndexMap.get(elem[0])).setScore(Integer.parseInt(elem[1]));
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
        informationBox.getChildren().add(textFlow);
    }



    /*
    Entry sheet controls
     */
    public void entryClickAction(){

    }

    /**
     * Method to construct elements of entry sheet
     * @return Array of objects of EntrySheetGUImplementation class
     */
    public EntrySheetGUImplementation[] makeEntrySheetElements(){

        String[] entryNames = {"ones", "twos", "threes", "fours", "fives", "sixes",
                "threeOfAKind", "fourOfAKind", "fullHouse", "smallStraight", "largeStraight",
                "kniffeliger", "chance", "pi"};

        EntrySheetGUImplementation[] entryElements = new EntrySheetGUImplementation[entryNames.length];

        int k = 0;
        for (String name : entryNames){
            //Begin ID number of entries at 1, such that ones = 1, twos = 2 etc.
            entryElements[k] = new EntrySheetGUImplementation(k+1, name);
            entrySheetNameIndexMap.put(name, k);
            k++;
        }
        return entryElements;
    }

    /**
     * Method that determines the required separation (tabulators) to align different strings
     * @param title String, on the length of which the required number of tabualators is determined
     * @param baselength Minimal number of tabulators added, e.g. separation added to longest String
     * @return String of containing a number of \t (tabulators), the number depends on the length of the layouted string
     */
    private static String fillWithTabulators(String title, int baselength) {
        String separation = "";
        //Align the scores by adjusting separation
        int titleLength = title.length();
        if ( title.equals("kniffeliger") ){
            separation = "\t".repeat(baselength + 1);
        }else if (titleLength >= 10) {
            separation = "\t".repeat(baselength);
        } else if (titleLength > 8) {
            separation = "\t".repeat(baselength + 1);
        } else if (titleLength > 3) {
            separation = "\t".repeat(baselength + 2);
        } else {
            separation = "\t".repeat(baselength + 3);
        }
        return separation;
    }

}
