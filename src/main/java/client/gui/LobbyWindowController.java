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

    private int lobbyCounter = 0;

    public void createLobbyAction(){
        String lobbyName = lobbyTextField.getText();
        ClientOutput.send(CommandsClientToServer.CRLO, lobbyName);
        createLobbyButton.setDisable(true);
        lobbyTextField.clear();
    }

    public void enterLobbyAction() {
        String[] splitLobbyAndStatus = selectedLobby.split(" ");
        ClientOutput.send(CommandsClientToServer.ENLO, splitLobbyAndStatus[0]);
    }

    public void leaveGame() {
        //TODO
    }

    public void selectLobby() {
        TreeItem<String> currentItem = lobbyList.getSelectionModel().getSelectedItem();

        if (currentItem != null) {
            selectedLobby = currentItem.getValue();
        }

        enterLobbyButton.setDisable(false);
    }

    public void initializeLobbyList(String lobbies) {
        TreeItem<String> dummyRoot = new TreeItem<>();

        lobbyList.setRoot(dummyRoot);
        lobbyList.setShowRoot(false);

        System.out.println("Lobbies: " + lobbies);

        if(lobbies.equals("")) {
            return;
        }

        String[] splitForLobbies = lobbies.split(",");
        for (int i = 0; i < splitForLobbies.length; i++) {
            String[] splitForPlayers = splitForLobbies[i].split(":");
            TreeItem<String> newLobby = new TreeItem<>(splitForPlayers[0]);
            lobbyList.getRoot().getChildren().add(newLobby);
            lobbyCounter++;
            for (int j = 1; j < splitForPlayers.length; j++) {
                TreeItem<String> newPlayer = new TreeItem<>(splitForPlayers[j]);
                newLobby.getChildren().add(newPlayer);
            }
        }
    }

    public void addLobby(String name) {
        TreeItem<String> newLobby = new TreeItem<>(name);
        lobbyList.getRoot().getChildren().add(newLobby);
        lobbyCounter++;
    }

    public void addPlayerToLobby(String lobbyAndPlayerName) {
        String[] splitInLobbyAndPlayer = lobbyAndPlayerName.split(":");

        for (int i = 0; i < lobbyCounter; i++) {
            if(lobbyList.getTreeItem(i).getValue().equals(splitInLobbyAndPlayer[0])) {
                System.out.println("Lobby found");
                TreeItem<String> newPlayer = new TreeItem<>(splitInLobbyAndPlayer[1]);
                lobbyList.getTreeItem(i).getChildren().add(newPlayer);
                return;
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Main.setLobbyWindowController(this);

        //on return from the server, the method initializeLobbyList will be called
        ClientOutput.send(CommandsClientToServer.LOLI, "get an initial lobby list");

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
    //TODO show players in lobby
}
