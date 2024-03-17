package client.manager;

import client.networking.ClientInput;
import client.networking.CommandsClientToServer;
import client.networking.ConsoleInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class GameManager {
    private ConsoleInput consoleInput;
    private ClientInput clientInput;
    private NetworkManagerClient networkManager;
    private GameLogicManager logicManager;

    /**
     * Constructor of GameManager.
     */
    public GameManager(String hostName, int port) {

        try {
            Socket socket = new Socket(hostName, port);
            // create manager
            networkManager = new NetworkManagerClient(socket);
            consoleInput = new ConsoleInput(networkManager);
            Thread consoleThread = new Thread(consoleInput);
            consoleThread.start();
            clientInput = new ClientInput(socket, this);
            Thread clientThread = new Thread(clientInput);
            clientThread.start();
            //logicManager = new GameLogicManager();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // print welcome text
        String welcomeText = "==============================================================\n" +
                "===                     Kniffeliger                        ===\n" +
                "==============================================================\n" +
                "Welcome to Kniffeliger TestDemo.";
        System.out.println(welcomeText);

        String username = System.getenv("USERNAME");
        //System.out.println("Your current username is: " + username);
        networkManager.send(CommandsClientToServer.CHNA, username);

        // start game
        this.start();
    }

    /**
     * This function starts the game.
     */
    private void start() {

    }



    /**
     * The main method.
     * @param args first param is hostname, second port
     */
    public static void main(String[] args) {
        GameManager gameManager = new GameManager(args[0], Integer.parseInt(args[1]));
    }
}
