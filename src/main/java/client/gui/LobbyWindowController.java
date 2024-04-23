package client.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is the controller class for the LobbyWindow, which lets the player create or enter a lobby.
 * It implements Initializable
 */
public class LobbyWindowController implements Initializable {
    
    private Logger logger = LogManager.getLogger(LobbyWindowController.class);

    @FXML
    private TreeView<String> lobbyList = new TreeView<>();

    @FXML
    private Button createLobbyButton;

    @FXML
    private Button enterLobbyButton;

    @FXML
    private Button leaveGameButton;
    
    @FXML
    private Button changeUsernameButton;

    @FXML
    private Button highScoreButton;

    @FXML
    private TextField lobbyTextField;
    
    @FXML
    private TextField usernameTextField;

    private String selectedLobby = null;

    private int lobbyCounter = 0;

    private boolean hasBeenInitialized = false;

    /**
     * Handles when the button createLobby is pressed, sends the request to create a new lobby to the server
     */
    public void createLobbyAction(){
        String lobbyName = lobbyTextField.getText();
        ClientOutput.send(CommandsClientToServer.CRLO, lobbyName);
        createLobbyButton.setDisable(true);
        lobbyTextField.clear();
    }

    /**
     * Handles when the button enterLobby is pressed, sends the request to enter a lobby to the server
     * @param event
     */
    public void enterLobbyAction(ActionEvent event) {
        String[] splitLobbyAndStatus = selectedLobby.split(" ");
        ClientOutput.send(CommandsClientToServer.ENLO, splitLobbyAndStatus[0]);
        enterLobbyButton.setDisable(true); //TODO deactivate button also when list is not selected?
        SceneController.switchToGameWindow(event);
    }

    /**
     * Handles when the user changes their username via the gui to the name put in via the text field
     * @param event
     */
    public void changeUsernameAction(ActionEvent event) {
        String username = usernameTextField.getText();
        ClientOutput.send(CommandsClientToServer.CHNA, username);
        changeUsernameButton.setDisable(true);
        usernameTextField.clear();
    }

    public void highScoreAction() {
        //TODO
    }

    /**
     * Handles when the button leaveGame is pressed
     */
    public void leaveGame() {
        Main.exit();
    }

    /**
     * Handles what happens when a Lobby in the TreeView is selected
     */
    public void selectLobby() {
        TreeItem<String> currentItem = lobbyList.getSelectionModel().getSelectedItem();

        if (currentItem != null) {
            //selects the current lobby to possibly enter
            selectedLobby = currentItem.getValue();
        }

        //enables the button to enter the chosen lobby
        enterLobbyButton.setDisable(false);
    }

    /**
     * Gets the initial list of existing lobbies from the server and displays it in the tree view
     * @param lobbies the list of lobbies from the server, it is of the form "Lobbie:player:player,Lobbie:player..."
     */
    public void initializeLobbyList(String lobbies) {

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

    /**
     * Adds a new Lobby to the TreeView
     * @param name
     */
    public void addLobby(String name) {
        TreeItem<String> newLobby = new TreeItem<>(name);
        lobbyList.getRoot().getChildren().add(newLobby);
        lobbyCounter++;
    }

    /**
     * Adds a new Player to a Lobby in the TreeView
     * @param lobbyAndPlayerName
     */
    public void addPlayerToLobby(String lobbyAndPlayerName) {
        String[] splitInLobbyAndPlayer = lobbyAndPlayerName.split(":");

        for (int i = 0; i < lobbyCounter; i++) {
            if(lobbyList.getTreeItem(i).getValue().equals(splitInLobbyAndPlayer[0])) {
                logger.debug("Lobby found");
                TreeItem<String> newPlayer = new TreeItem<>(splitInLobbyAndPlayer[1]);
                lobbyList.getTreeItem(i).getChildren().add(newPlayer);
                return;
            }
        }
    }

    /**
     * Removes a player from a lobby in the TreeView
     * @param lobbyAndPlayerName
     */
    public void removePlayerFromList(String lobbyAndPlayerName) {
        String[] splitInLobbyAndPlayer = lobbyAndPlayerName.split(":");
        logger.debug("lobby: " + splitInLobbyAndPlayer[0]);

        for (TreeItem<String> treeItem : lobbyList.getRoot().getChildren()) {
            logger.debug("Tree item found, " + treeItem.getValue());
            if(treeItem.getValue().equals(splitInLobbyAndPlayer[0])) {
                logger.debug("lobby found");
                for (TreeItem<String> playerInLobby : treeItem.getChildren()) {
                    if (playerInLobby.getValue().equals(splitInLobbyAndPlayer[1])) {
                        logger.debug("player found");
                        playerInLobby.getParent().getChildren().remove(playerInLobby);
                        return;
                    }
                }
            }
        }

    }

    /**
     * The initialize Method for the Lobby Window
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
        logger.info("The Lobby Window has been initialized");

        Main.setLobbyWindowController(this);

        if (!hasBeenInitialized) {
            TreeItem<String> dummyRoot = new TreeItem<>();

            lobbyList.setRoot(dummyRoot);
            lobbyList.setShowRoot(false);
        }

        //on return from the server, the method initializeLobbyList will be called
        ClientOutput.send(CommandsClientToServer.LOLI, "get an initial lobby list");

        createLobbyButton.setDisable(true);
        enterLobbyButton.setDisable(true);
        changeUsernameButton.setDisable(true);

        //TODO make this pretty
        lobbyTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (lobbyTextField.isFocused()) {
                    createLobbyButton.setDisable(false);
                }
            }
        });

        usernameTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (usernameTextField.isFocused()) {
                    changeUsernameButton.setDisable(false);
                }
            }
        });

        hasBeenInitialized = true;
    }

    //TODO implement high score list
    //TODO only lobbies, not players can be selected
    //TODO popUps when something is not done right?
    //TODO status of lobby is correct
}
