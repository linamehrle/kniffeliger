package client.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import client.networking.ClientOutput;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import server.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import static server.networking.Communication.sendWhisper;

/**
 * This class implements the controller for the chatwindow GUI
 * the names for the @FXML variables can be found in the corresponding FXML file under fx:id
 */
public class CWcontroller implements Initializable {
    @FXML
    private Button buttonSend;
    //text field where message can be entered
    @FXML
    private TextField msgAcceptor;
    @FXML
    VBox msgDisplayAll;
    //choice box to chose recipient
    @FXML
    private ChoiceBox recID;
    private Player player;
    private String recipient;
    private List<String> userNameList;
    ArrayList<Player> playerList;
    private ClientOutput networkManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //this.networkManager = ;
        // will be used later to select user for whisper chat TODO: replace by real usernames
        String[] userList = {"all", "usr1", "usr2", "usr3"};
        //playerList .getPlayerList();
        //userNameList = makeUsernameList(playerList);
        //cast String[] to Object[] to remove warning when compiling
        setChoiceBox(recID, (Object[])userList);

        //set default recipient to all (corresponds to \chat in console)
        setRecipient("all");
        recID.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            // if the item of the list is changed
            public void changed(ObservableValue ov, Number value, Number new_value) {
                // set recipient to selected user
                recipient = userList[new_value.intValue()];
            }
        });

        //set up send button TODO: add exception handling
        buttonSend.setOnAction(new EventHandler<ActionEvent>() {@Override
            public void handle(ActionEvent event) {
                sendButtonAction();
            }
        });
            //this enables the ENTER key to fire SEND button (not good method for complex GUIs)
        buttonSend.setDefaultButton(true);
        networkManager = ChatWindow.getNetworkManager();
    }

    /** Method which handles the addition of text to chat window
     *  and sends message to server when send button is fired
     */
    public void sendButtonAction() {
        //read message from input text field
        String messageToSend = msgAcceptor.getText();
        if (!msgAcceptor.getText().isEmpty()) {
            //add send message to message display field
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);

            hBox.setPadding(new Insets(5, 5, 5, 10));
            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle(
                    "-fx-color: rgb(239, 242, 255);" +
                            "-fx-background-color: rgb(15, 125, 242);" +
                            "-fx-background-radius: 20px;");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.925, 0.996));

            hBox.getChildren().add(textFlow);
            msgDisplayAll.getChildren().add(hBox);

            //TODO: sendMsgtoServer(, getRecipient(), messageToSend);
            sendMsgtoServer(messageToSend);

            msgAcceptor.clear();
        }

    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    private class getClickID implements EventHandler<Event>{
        @Override
        public void handle(Event evt) {
            System.out.println(((Control)evt.getSource()).getId());
        }
    }

    /**
     * This method adds text to a VBox. It is used to display received messages
     * @param messageFromServer message received from server
     * @param displayLocation VBox where message is displayed (usually main VBox)
     */
    //display message from server TODO: display messages from different users in different ways
    public static void addText(String messageFromServer, VBox displayLocation){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(
                "-fx-background-color: rgb(233, 233, 235);" +
                        "-fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                displayLocation.getChildren().add(hBox);
            }
        });
    }

    /**
     * method to send messages to server, this is the standard method to send messages over the chatwindow
     * TODO: infer command from choice in choicebox (i.e. CHAT if 'all' is selected, WHSP if 'user' is selected
     * @param messageToSend
     */
    public void sendMsgtoServer(String messageToSend){
        //String messageCombined = receiver.getUsername() + messageToSend;
        //sendWhisper(sender, messageCombined);
        this.networkManager.sendToServer(messageToSend);
        System.out.println(messageToSend);
    }


    public static List<String> makeUsernameList(ArrayList<Player> playerList){
        List<String> userNameList = new ArrayList<>();
        for (Player player : playerList) {
            userNameList.add(player.getUsername());
        }
        return userNameList;
    }

    public static void setChoiceBox(ChoiceBox choiceBox, Object[] values){
        choiceBox.getItems().addAll(values);
    }
    public void setNetworkManager(ClientOutput clientOutput){
        this.networkManager = clientOutput;
    }
}