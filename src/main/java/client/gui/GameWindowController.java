package client.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;

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
    private Button startButton;
    @FXML
    private Button leaveGameButton;
    @FXML
    private Button leaveLobbyButton;
    @FXML
    private Button rollButton;
    @FXML
    private Button enterButton;

    @FXML
    private CheckBox die1Checkbox;
    @FXML
    private CheckBox die2Checkbox;
    @FXML
    private CheckBox die3Checkbox;
    @FXML
    private CheckBox die4Checkbox;
    @FXML
    private CheckBox die5Checkbox;

    @FXML
    private TableView<String> entrySheet;
    @FXML
    private TableView<String> dice;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //hier einträge im entry sheet

        entrySheet.setDisable(true);
        dice.setDisable(true);

        die1Checkbox.setDisable(true);
        die2Checkbox.setDisable(true);
        die3Checkbox.setDisable(true);
        die4Checkbox.setDisable(true);
        die5Checkbox.setDisable(true);

        stealButton.setDisable(true);
        freezeButton.setDisable(true);
        rotateButton.setDisable(true);
        rollButton.setDisable(true);
        swapButton.setDisable(true);
        deleteButton.setDisable(true);
        rollButton.setDisable(true);
        enterButton.setDisable(true);


    }

    public void saveDie1(ActionEvent event) {
    }

    public void saveDie2(ActionEvent event) {
    }

    public void saveDie3(ActionEvent event) {
    }

    public void saveDie4(ActionEvent event) {
    }

    public void saveDie5(ActionEvent event) {
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
    }

    public void leaveLobbyAction(ActionEvent event) {
        ClientOutput.send(CommandsClientToServer.LELO, "bye Lobby");
        SceneController.switchToLobbyWindow(event);
    }

    public void enterToEntrySheetAction(ActionEvent event) {
    }
}
