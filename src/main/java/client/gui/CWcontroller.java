package client.gui;

import java.util.ArrayList;
import java.util.List;

import client.networking.ClientOutput;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import server.Player;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * This class implements the controller for the chatwindow GUI
 * the names for the @FXML variables can be found in the corresponding chatwindow.FXML file under fx:id
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
    private ChoiceBox<String> recID;
    //private Player player;
    private String recipient;
    //private List<String> userNameList;
    //ArrayList<Player> playerList;
    //private ClientOutput networkManager;

    /**
     * Initializer for the ChatWindow is called when FXML is loaded
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Main.setcWcontroller(this);

        //TODO: replace by real usernames, maybe use dictionary structure with userID as key, such that username can be changed without changing display order and colour
        //will be used later to select user for whisper chat
        String[] userList = {"all", "usr1", "usr2", "usr3"};


        //playerList .getPlayerList();
        //userNameList = makeUsernameList(playerList);
        //cast String[] to Object[] to remove warning when compiling
        setChoiceBox(recID, userList);

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

    }



    /** Method which handles the addition of text to chat window
     *  and sends message to server when send button is fired
     */
    public void sendButtonAction() {
        //read message from input text field
        String messageToSend = msgAcceptor.getText();
        if (!msgAcceptor.getText().isEmpty()) {
            // add sent message to message display field (right side, in light gray)
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
            // relay message to server
            sendMsgtoServer(messageToSend);
            // clear text field, where message is entered
            msgAcceptor.clear();
        }

    }

    /**
     * getter for the recipent field that stores the username currently selected in ChoiceBox recID
     * @return value of recipient field
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * setter recipent field that stores the username currently selected in ChoiceBox recID
     * used for updating recipient field when selection in ChoiceBox recID is changed
     * @param recipient username from list of usernames to set the recipient field with
     */

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    //private class getClickID implements EventHandler<Event>{
        //@Override
        //public void handle(Event evt) {
            //}
    //}

    /**
     * This method adds text to a VBox. It is used to display received messages
     * @param messageFromServer message received from server
     * @param displayLocation VBox where message is displayed (usually main VBox)
     */
    //display message from server TODO: display messages from different users in different ways
    public void addMsgReceived(String messageFromServer, VBox displayLocation){
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
     * method to send messages to server, this is the standard method to send messages over the chat window
     * @param message message to send, contains @all, @lobby or @username at the beginning followed by a space
     */
    public void sendMsgtoServer(String message){

        //at the moment the default is to send a message to everybody (if no @... is at the beginning)
        if (message.charAt(0) != '@') {
            ClientOutput.sendToServer("CHAT " + message);
            return;
        }

        //String receiver = getRecipient();
        //assemble message to server, use command CHAT if 'all' or '' is selected in ChoiceBox
        //TODO: add error handling for unkown user names (although this should never occur)

        String[] splitMessage = message.split(" ", 2);
        String receiver = splitMessage[0];
        String messageBody = splitMessage[1];

        switch(receiver) {
            case "@lobby":
                ClientOutput.sendToServer("LOCH " + messageBody);
                break;
            case "@all":
                ClientOutput.sendToServer("CHAT " + messageBody);
                break;
            default:
                ClientOutput.sendToServer("WHSP " + receiver.substring(1) + " " + messageBody);
        }

    }

    /**
     * method to transfer list of players from server to GUI
     * @param playerList list of players
     * @return list of strings with user names
     */
    public static List<String> makeUsernameList(ArrayList<Player> playerList){
        List<String> userNameList = new ArrayList<>();
        for (Player player : playerList) {
            userNameList.add(player.getUsername());
        }
        return userNameList;
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
    //public void setNetworkManager(ClientOutput clientOutput){
        //this.networkManager = clientOutput;
    //}

    /**
     * method to display message in main display area of chat window (on left side in light gray)
     * @param message message to be displayed
     */
    public void displayReceivedMessage(String message) {
        //display this in the chat window, message is "username: message"
        addMsgReceived(message, msgDisplayAll);
    }
}