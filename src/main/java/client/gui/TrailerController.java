package client.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import org.apache.logging.log4j.Logger;
import starter.Starter;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller class for TrailerWindow.fxml
 * GUI window that plays the trailer
 */
public class TrailerController implements Initializable {

    private static Logger logger = Starter.getLogger();
    private LobbyWindowController lobbyWindowController;

    @FXML
    private MediaView trailerMediaView;
    private MediaPlayer trailerPlayer;



    public void initialize(URL location, ResourceBundle resources) {

        lobbyWindowController = Main.getLobbyWindowController();
        // Load Roll button animation
        try {
            trailerPlayer = GameWindowHelper.loadMedia("trailer.mp4");
            logger.trace("Animation file 'trailer.mp4' loaded.");
        } catch (FileNotFoundException e) {
            logger.info("Animation file 'trailer.mp4' not found.");
        }

        trailerMediaView.setMediaPlayer(trailerPlayer);

        // Open LobbyWindow after trailer has finished playing
        trailerPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                SceneController.switchToLobbyWindow(new ActionEvent());
            }
        });

        trailerPlayer.play();

       // lobbyWindowController.setThemeVolume(0.1);

    }
}

