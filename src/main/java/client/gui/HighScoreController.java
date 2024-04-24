package client.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;

public class HighScoreController implements Initializable {

    @FXML
    private ListView<String> highScoreListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.setHighScoreController(this);
        ClientOutput.send(CommandsClientToServer.HGSC, "getting th high score list");
        System.out.println("test");
    }
}
