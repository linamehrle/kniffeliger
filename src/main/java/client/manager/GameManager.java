package client.manager;

import client.util.TerminalView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class represents the centrepiece of the client. It is designed to manage between the networking and GUI. The
 * GameManager also serves as starting point for the client.
 */
public class GameManager {
    // Manager
    private NetworkManagerClient networkManager;

    /**
     * This method constructs the game manager of the client with given parameter given from the main method.
     * @param hostName host address to connect to
     * @param port port to connect to
     * @param username name of the user
     */
    public GameManager(String hostName, int port, String username) {
        // create manager
        networkManager = new NetworkManagerClient(hostName, port);

        // print welcome text
        String welcomeText = """
                ==============================================================
                ===                     Kniffeliger                        ===
                ==============================================================
                Welcome to Kniffeliger TestDemo.""";

        TerminalView.printlnText(welcomeText);

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
            TerminalView.printlnText(e.getMessage());
        }

        networkManager.close();
    }
}
