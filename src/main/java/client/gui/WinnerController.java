package client.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * This is the controller class for the pop-up that shows the winner at the end of the game
 */
public class WinnerController implements Initializable {

    @FXML
    private Label winnerLabel;

    /**
     * Sets the winner to display
     * @param winner
     */
    public void setWinner(String winner) {
        String winnerAndPoints = winner.split(",")[0];
        winnerLabel.setText(winnerAndPoints.split(" ")[0]);
    }

    /**
     * Initiates the window
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
        SceneController.setWinnerController(this);
    }
}
