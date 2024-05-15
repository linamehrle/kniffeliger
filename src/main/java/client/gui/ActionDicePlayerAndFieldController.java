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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import animatefx.animation.Flash;
import animatefx.animation.Wobble;
import animatefx.animation.ZoomOut;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;

/**
 * This is the controller class for the pop-up window that lets a player choose the victim and the entry for the action dices
 * steal, freeze and delete. It implements Initializable.
 */
public class ActionDicePlayerAndFieldController implements Initializable {

    @FXML
    private ChoiceBox playerChoiceBox;

    @FXML
    private ChoiceBox fieldChoiceBox;

    @FXML
    private Label questionLabel;

    @FXML
    private Button okayButton;

    private ArrayList<String> playerList;
    private String[] entryList = new String[] {"ones", "twos", "threes", "fours", "fives", "sixes",
                                                "threeOfAKind", "fourOfAKind", "fullHouse", "smallStraight",
                                                "largeStraight", "kniffeliger", "chance", "pi"};

    private String action;

    private Wobble freezeAnimationWobble;
    private ZoomOut deleteAnimationSwing;
    private Flash stealAnimationFlash;

    /**
     * When the okay button is hit, the choice made is sent to the server and the window is closed.
     * @param event
     */
    public void okayButtonAction(ActionEvent event) {

        if (playerChoiceBox.getValue() == null || fieldChoiceBox.getValue() == null) {
            System.out.println("choose entries");
            return;
        }

        switch (action) {
            case "freeze" -> {
                ClientOutput.send(CommandsClientToServer.FRZE,
                        playerChoiceBox.getValue() + " " + fieldChoiceBox.getValue());
                freezeAnimationWobble.play();
            }
            case "steal" -> {
                ClientOutput.send(CommandsClientToServer.STEA,
                        playerChoiceBox.getValue() + " " + fieldChoiceBox.getValue());
                stealAnimationFlash.play();
            }
            case "delete" -> {
                ClientOutput.send(CommandsClientToServer.COUT,
                        playerChoiceBox.getValue() + " " + fieldChoiceBox.getValue());
                deleteAnimationSwing.play();
            }
        }

        Stage stage = (Stage) okayButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Initiates the choice boxes with the current player list and entries as well as the question text at the top
     * @param playerList the current players in the game
     * @param action the action that will be performed, i.e. "freeze" or "steal" etc.
     */
    public void setUp(ArrayList<String> playerList, String action, ListView<String> entrySheet) {

        this.action = action;
        this.playerList = playerList;

        String[] playerArray = new String[playerList.size()];
        for (int i = 0; i < playerArray.length; i++) {
            playerArray[i] = playerList.get(i);
        }

        setChoiceBox(fieldChoiceBox, entryList);
        setChoiceBox(playerChoiceBox, playerArray);

        questionLabel.setText("What do you want to " + action+ "?");

        //initiate animations
        stealAnimationFlash = new Flash(entrySheet);
        stealAnimationFlash.setResetOnFinished(true);

        freezeAnimationWobble = new Wobble(entrySheet);
        freezeAnimationWobble.setResetOnFinished(true);

        deleteAnimationSwing = new ZoomOut(entrySheet);
        deleteAnimationSwing.setResetOnFinished(true);
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
        SceneController.setActionDicePlayerAndFieldWindow(this);
    }
}
