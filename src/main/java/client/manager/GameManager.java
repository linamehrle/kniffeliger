package client.manager;

import client.util.TerminalView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameManager {
    // Manager
    private NetworkManagerClient networkManager;
    private GameLogicManager logicManager;

    /**
     * Constructor of GameManager.
     */
    public GameManager(String hostName, int port) {
        // create manager
        networkManager = new NetworkManagerClient(hostName, port);
        logicManager = new GameLogicManager();

        // print welcome text
        String welcomeText = "==============================================================\n" +
                "===                     Kniffeliger                        ===\n" +
                "==============================================================\n" +
                "Welcome to Kniffeliger TestDemo.";
        TerminalView.printText(welcomeText);

        // start game
        this.start();
    }

    /**
     * This function starts the game.
     */
    private void start() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {
                String consoleIn = in.readLine();

                // if quit was entered
                if (consoleIn.equalsIgnoreCase("QUIT")) {
                    break;
                }

                // send data to echo server
                networkManager.sendToServer(consoleIn);
            }
        } catch(IOException e) {
            TerminalView.printText(e.getMessage());
        }

        networkManager.close();
    }



    /**
     * The main method.
     * @param args first param is hostname, second port
     */
    public static void main(String[] args) {
        GameManager gameManager = new GameManager(args[0], Integer.parseInt(args[1]));
    }
}
