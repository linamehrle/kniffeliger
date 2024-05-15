package client.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import animatefx.animation.RotateOutDownLeft;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;

/**
 * This is the controller class for the pop-up window that lets a player choose the victim for the action die swap.
 * It implements Initializable.
 */
public class ActionDicePlayerWindow implements Initializable {
    @FXML
    private ChoiceBox playerChoiceBox;
    @FXML
    private Button okayButton;

    private String[] playerArray;

    private RotateOutDownLeft swapAnimationFade;

    /**
     * When the okay button is hit, the choice made is sent to the server and the window is closed.
     * @param event
     */
    public void okayButtonAction(ActionEvent event) {
        if (playerChoiceBox.getValue() == null) {
            System.out.println("choose entries");
            return;
        }

        ClientOutput.send(CommandsClientToServer.SWAP, (String) playerChoiceBox.getValue());

        swapAnimationFade.play();

        Stage stage = (Stage) okayButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Initiates the choice boxes with the current player list and entries as well as the question text at the top
     * @param playerList the current players in the game
     */
    public void setUp(ArrayList<String> playerList, ListView<String> entrySheet) {

        playerArray = new String[playerList.size()];
        for (int i = 0; i < playerArray.length; i++) {
            playerArray[i] = playerList.get(i);
        }

        setChoiceBox(playerChoiceBox, playerArray);

        swapAnimationFade = new RotateOutDownLeft(entrySheet);
        swapAnimationFade.setResetOnFinished(true);
    }

    /**
     * method to set
     * @param choiceBox JavaFX ChoiceBox to be set
     * @param values values update choice box with as string array
     */
    public static void setChoiceBox(ChoiceBox<String> choiceBox, String[] values) {
        ObservableList<String> userNameList = FXCollections.observableArrayList();
        choiceBox.setItems(userNameList);
        userNameList.addAll(values);
    }

    /**
     * Initialises the pop-up window.
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
        SceneController.setActionDicePlayerWindow(this);
    }
}
