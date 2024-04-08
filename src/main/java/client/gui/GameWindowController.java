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

public class GameWindowController implements Initializable {

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

        diceTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(diceTextField.isFocused()){
                    saveDiceButton.setDisable(false);
                }
            }
        });

    }


    public void stealEntryAction(ActionEvent event) {
    }

    public void freezeEntryButton(ActionEvent event) {
    }

    public void rotateSheetsAction(ActionEvent event) {
    }

    public void swapSheetsAction(ActionEvent event) {
    }

    public void deleteEntryAction(ActionEvent event) {
    }

    public void startGameAction(ActionEvent event) {
        ClientOutput.send(CommandsClientToServer.STRT, "lets start the game :)");
    }

    public void leaveGameAction(ActionEvent event) {
        Main.exit();
    }

    public void leaveLobbyAction(ActionEvent event) {
        ClientOutput.send(CommandsClientToServer.LELO, "bye Lobby");
        SceneController.switchToLobbyWindow(event);
    }

    public void enterToEntrySheetAction(ActionEvent event) {
    }

    public void saveDiceAction(ActionEvent event) {
    }
}
