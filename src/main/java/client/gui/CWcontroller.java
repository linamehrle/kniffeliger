package client.gui;

import java.awt.*;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // will be used later to select user for whisper
        recID.getItems().addAll("all", "usr1", "usr2");

        //set up send button TODO: add exception handling
        buttonSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendButtonAction();
        }
    });
        //this enables the ENTER key to fire SEND button (not good method for complex GUIs)
        buttonSend.setDefaultButton(true);

    }

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

            //TODO: send message to server


            msgAcceptor.clear();
        }

    }

    private class getClickID implements EventHandler<Event>{
        @Override
        public void handle(Event evt) {
            System.out.println(((Control)evt.getSource()).getId());
        }
    }

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

    public static void sendMsgtoServer(String messageToServer){

    }
}