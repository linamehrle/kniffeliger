package client.manager;

import client.util.TerminalView;

public class GameManager {
    // Manager
    private NetworkManager networkManager;
    private TerminalView terminalView;

    // marks the current "scene", for now 0: MainMenu, 1: IN_GAME
    private int currentScene;

    /**
     * Constructor of GameManager.
     */
    public GameManager() {
        // create manager
        networkManager = new NetworkManager();
        terminalView = new TerminalView();

        // init
        currentScene = 0;

        // start game
        this.start();
    }

    /**
     * This function starts the game.
     */
    private void start() {
        // ask if a game should be started
    }



    /**
     * The main method.
     * @param args
     */
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
    }
}
