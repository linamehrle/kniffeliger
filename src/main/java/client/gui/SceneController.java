package client.gui;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.Logger;
import starter.Starter;

/**
 * This class controls which window is open in the gui
 */
public class SceneController {
    private static Logger logger = Starter.getLogger();

    private static Stage mainWindow;

    private static Scene highScoreWindow;
    private static Scene scene;
    private static Parent root;

    /**
     * Switchen from the game window back to the lobby window when a player leaves a lobby
     * @param event
     */
    public static void switchToGameWindow(ActionEvent event) {
        logger.info("Switching to the game window");
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/GameWindow.fxml"));
            root = loader.load();
            scene = new Scene(root);
            mainWindow.setScene(scene);
            mainWindow.show();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * Switches from the lobby window to the game window when a player enters a lobby
     * @param event
     */
    public static void switchToLobbyWindow(ActionEvent event) {
        logger.info("switching to the lobby window");
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/LobbyWindow.fxml"));
            root = loader.load();
            highScoreWindow = new Scene(root);
            mainWindow.setScene(scene);
            mainWindow.show();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    public static void showHighScoreWindow() {
        logger.info("trying to open the high score window");
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/HighScoreWindow.fxml"));
            Parent root2 = loader.load();
            Stage stage = new Stage();
            scene = new Scene(root2);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * Setter for the main Window of the game
     * @param mainWindow
     */
    public static void setMainWindow(Stage mainWindow) {
        SceneController.mainWindow = mainWindow;
    }
}
