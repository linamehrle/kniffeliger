package client.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {

    private static Stage mainWindow;
    private static Scene scene;
    private static Parent root;

    public static void switchToGameWindow(ActionEvent event) {
        try {
            /*URL url = new File("src/main/resources/GameWindow.fxml").toURI().toURL();
            root = FXMLLoader.load(url);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
             */

            //von dominique:
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/GameWindow.fxml"));
            root = loader.load();

            scene = new Scene(root);
            mainWindow.setScene(scene);
            mainWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchToLobbyWindow(ActionEvent event) {
        try {
            //URL url = new File("src/main/resources/LobbyWindow.fxml").toURI().toURL();
            //root = FXMLLoader.load(url);
            //stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            //von dominique:
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/LobbyWindow.fxml"));
            root = loader.load();

            scene = new Scene(root);
            mainWindow.setScene(scene);
            mainWindow.show();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public static void setMainWindow(Stage mainWindow) {
        SceneController.mainWindow = mainWindow;
    }
}
