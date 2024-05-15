package client.gui;

import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
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
            if (gameWindowController != null) {
                gameWindowController.muteMainTheme();
            }
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/LobbyWindow.fxml"));
            root = loader.load();
            scene = new Scene(root, 809, 500);
            mainWindow.setScene(scene);
            mainWindow.show();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    public static void changeToTrailerWindow(ActionEvent event)  {
        logger.info("switching to the trailer window");

        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/TrailerWindow.fxml"));
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
    public static void showActionPlayerAndFieldWindow(ArrayList<String> playerList, String action, ListView<String> entrySheet) {
        logger.info("trying to open the action dice window");
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/ActionDicePlayerAndFieldWindow.fxml"));
            Parent root3 = loader.load();
            Stage stage = new Stage();
            scene = new Scene(root3, 300, 200);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        actionDicePlayerAndFieldWindow.setUp(playerList, action, entrySheet);
    }

    /**
     * Opens the ActionDicePlayerWindow
     * @param playerList
     */
    public static void showActionPlayerWindow(ArrayList<String> playerList, ListView<String> entrySheet) {
        logger.info("trying to open the action dice window");
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/ActionDicePlayerWindow.fxml"));
            Parent root3 = loader.load();
            Stage stage = new Stage();
            scene = new Scene(root3, 300, 200);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        actionDicePlayerWindow.setUp(playerList, entrySheet);
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
     * This method opens a scroll pane which shows a png file with information about the game
     */
    public static void openInfoWindow() {
        Image image = new Image("/images/Anleitung.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1000);
        ScrollPane scrollPane = new ScrollPane(imageView);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        StackPane root = new StackPane();
        root.getChildren().add(scrollPane);
        Stage stage = new Stage();
        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.show();
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
