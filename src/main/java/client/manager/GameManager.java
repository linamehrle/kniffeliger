package client.manager;

import client.networking.ClientInput;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;
import client.networking.ConsoleInput;
import client.networking.Pong;

import java.io.IOException;
import java.net.Socket;

public class GameManager {

    private Socket socket;
    private ConsoleInput consoleInput;
    private ClientInput clientInput;
    private ClientOutput clientOutput;
    private Pong pong;
    private GameLogicManager logicManager;

    /**
     * Constructor of GameManager.
     */
    public GameManager(String hostName, int port) {

        try {
            socket = new Socket(hostName, port);
            // create manager
            clientOutput = new ClientOutput(socket);

            consoleInput = new ConsoleInput(clientOutput);
            Thread consoleThread = new Thread(consoleInput);
            consoleThread.start();

            clientInput = new ClientInput(socket, this);
            Thread clientThread = new Thread(clientInput);
            clientThread.start();

            pong = new Pong(this);
            Thread pongThread = new Thread(pong);
            pongThread.start();

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
        clientOutput.send(CommandsClientToServer.CHNA, username);

        // start game
        this.start();
    }

    /**
     * This function starts the game.
     */
    private void start() {

    }

    public void disconnect() {
        try {
            consoleInput.stop();
            clientInput.stop();
            pong.stop();
            socket.close();
            clientOutput.stop();
            System.out.println("Goodbye!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //andere auch mit this und getter??
    public ClientOutput getClientOutput() {
        return clientOutput;
    }

    public Pong getPong() {
        return pong;
    }


    /**
     * The main method.
     * @param args first param is hostname, second port
     */
    public static void main(String[] args) {
        GameManager gameManager = new GameManager(args[0], Integer.parseInt(args[1]));
    }
}
