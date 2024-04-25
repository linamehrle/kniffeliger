package client.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;

/**
 * This class is the controller class for the high score window which displays the current high score on demand
 */
public class HighScoreController implements Initializable {

    @FXML
    private ListView<String> highScoreListView = new ListView<>();

    /**
     * Method to display the high score via the listView
     * @param highScore
     */
    public void updateHighScore(String highScore) {

        if (highScore.equals("")) {
            return;
        }

        String[] scores = highScore.split(",");
        for (int i = 0; i < scores.length; i++) {
            String name = scores[i].split(":")[1];
            String points = scores[i].split(":")[0];
            highScoreListView.getItems().add((i+1) + ". " + name + ": " + points + " Punkte");
        }

    }

    /**
     * Method to initialize the high score window. It requests a current high score from the server to display.
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
        Main.setHighScoreController(this);
        ClientOutput.send(CommandsClientToServer.HGSC, "getting th high score list");
    }
}
