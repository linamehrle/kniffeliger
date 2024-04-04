package client.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;

public class LobbyWindowController implements Initializable {

    @FXML
    private TreeView<String> lobbyList = new TreeView<>();

    @FXML
    private Button createLobbyButton;

    @FXML
    private Button enterLobbyButton;

    @FXML
    private TextField lobbyTextField;

    private String selectedLobby = null;

    public void createLobbyAction(){
        String lobbyName = lobbyTextField.getText();
        ClientOutput.send(CommandsClientToServer.CRLO, lobbyName);
        createLobbyButton.setDisable(true);
    }

    public void enterLobbyAction() {
        ClientOutput.send(CommandsClientToServer.ENLO, selectedLobby);
    }

    public void leaveGame() {
        //TODO
    }

    public void selectLobby() {
        TreeItem<String> currentItem = lobbyList.getSelectionModel().getSelectedItem();

        if (!currentItem.equals(null)) {
            selectedLobby = currentItem.getValue();
        }

        enterLobbyButton.setDisable(false);
    }

    public void addLobby(String name) {
        System.out.println("Lobby received: " + name);
        TreeItem<String> newLobby = new TreeItem<>(name);
        lobbyList.getRoot().getChildren().add(newLobby);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Main.setLobbyWindowController(this);

        TreeItem<String> dummyRoot = new TreeItem<>();

        lobbyList.setRoot(dummyRoot);
        lobbyList.setShowRoot(false);

        createLobbyButton.setDisable(true);
        enterLobbyButton.setDisable(true);

        //TODO make this pretty
        lobbyTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(lobbyTextField.isFocused()){
                    createLobbyButton.setDisable(false);
                }
            }
        });



    }

    //TODO only lobbies, not players can be selected
    //TODO button enterLobby is deactivated when no list item is selected
    //TODO popUps when something is not done right
    //TODO lobby only works when one already exists?
    //TODO lobby status
    //TODO show players in lobby
}
