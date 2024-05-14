package client.gui;

import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
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
    private static Scene scene;
    private static Parent root;

    private static ActionDicePlayerAndFieldController actionDicePlayerAndFieldWindow;
    private static ActionDicePlayerWindow actionDicePlayerWindow;
    private static WinnerController winnerController;

    /**
     * Switchen from the game window back to the lobby window when a player leaves a lobby
     * @param event
     */
    public static void switchToGameWindow(ActionEvent event) {
        logger.info("Switching to the game window");
        try {
            LobbyWindowController lobbyWindowController = Main.getLobbyWindowController();
            lobbyWindowController.muteMainTheme();
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/GameWindow.fxml"));
            root = loader.load();
            scene = new Scene(root, 1133, 700);
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
            GameWindowController gameWindowController = Main.getGameWindowController();
            gameWindowController.muteMainTheme();
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/LobbyWindow.fxml"));
            root = loader.load();
            scene = new Scene(root, 809, 500);
            mainWindow.setScene(scene);
            mainWindow.show();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * Method that opens the high score pup up window.
     */
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
     * Opens the ActionDicePlayerAndFieldWindow
     * @param playerList
     * @param action
     */
    public static void showActionPlayerAndFieldWindow(ArrayList<String> playerList, String action) {
        logger.info("trying to open the action dice window");
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/ActionDicePlayerAndFieldWindow.fxml"));
            Parent root3 = loader.load();
            Stage stage = new Stage();
            stage.setMaximized(true);
            scene = new Scene(root3);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        actionDicePlayerAndFieldWindow.setUp(playerList, action);
    }

    /**
     * Opens the ActionDicePlayerWindow
     * @param playerList
     */
    public static void showActionPlayerWindow(ArrayList<String> playerList) {
        logger.info("trying to open the action dice window");
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/ActionDicePlayerWindow.fxml"));
            Parent root3 = loader.load();
            Stage stage = new Stage();
            scene = new Scene(root3);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        actionDicePlayerWindow.setUp(playerList);
    }

    /**
     * Opens the WinnerWindow once the game is over
     * @param winner the winner to be displayed in the window
     */
    public static void showWinnerWindow(String winner) {
        logger.info("trying to open the winner window");
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/WinnerWindow.fxml"));
            Parent root3 = loader.load();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Stage stage = new Stage();
                    scene = new Scene(root3);
                    stage.setScene(scene);
                    stage.show();
                }
            });

        } catch (IOException e) {
            logger.warn(e.getMessage());
        }

        winnerController.setWinner(winner);
    }

    /**
     * Setter for the main Window of the game
     * @param mainWindow
     */
    public static void setMainWindow(Stage mainWindow) {
        SceneController.mainWindow = mainWindow;
    }

    /**
     * Setter for the ActionDicePlayerAndFieldWindow
     * @param actionDicePlayerAndFieldWindow
     */
    public static void setActionDicePlayerAndFieldWindow(ActionDicePlayerAndFieldController actionDicePlayerAndFieldWindow) {
        SceneController.actionDicePlayerAndFieldWindow = actionDicePlayerAndFieldWindow;
    }

    /**
     * Setter for the ActionDicePlayerWindow
     * @param actionDicePlayerWindow
     */
    public static void setActionDicePlayerWindow(ActionDicePlayerWindow actionDicePlayerWindow) {
        SceneController.actionDicePlayerWindow = actionDicePlayerWindow;
    }

    /**
     * Setter for the winnerController
     * @param winnerController
     */
    public static void setWinnerController(WinnerController winnerController) {
        SceneController.winnerController = winnerController;
    }


}
