package client.manager;

import client.util.TerminalView;

public class GameManager {
    // Manager
    private NetworkManager networkManager;
    private TerminalView terminalView;
    private GameLogicManager logicManager;

    /**
     * Constructor of GameManager.
     */
    public GameManager() {
        // create manager
        networkManager = new NetworkManager();
        terminalView = new TerminalView();
        logicManager = new GameLogicManager();

        // start game
        this.start();
    }

    /**
     * This function starts the game.
     */
    private void start() {
        // TODO: start game over logicManager
    }



    /**
     * The main method.
     * @param args
     */
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
    }
}
