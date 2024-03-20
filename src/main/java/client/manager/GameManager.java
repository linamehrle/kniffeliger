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

    //private GameLogicManager logicManager; for later

    /**
     * Constructor of GameManager.
     */
    public GameManager(String hostName, int port) {

        try {
            socket = new Socket(hostName, port);
            // create manager
            clientOutput = new ClientOutput(socket);

            // auch einfach this übergeben?
            consoleInput = new ConsoleInput(clientOutput);
            Thread consoleThread = new Thread(consoleInput);
            consoleThread.start();

            clientInput = new ClientInput(socket, this);
            Thread clientThread = new Thread(clientInput);
            clientThread.start();

            pong = new Pong(this);
            Thread pongThread = new Thread(pong);
            pongThread.start();

            //logicManager = new GameLogicManager(); for later
        } catch (IOException e) {
            System.out.println("no server found\ngoodbye!");
            //e.printStackTrace();
            return;
        }

        // print welcome text
        String welcomeText = "======================================================================\n" +
                "====                        Kniffeliger                           ====\n" +
                "======================================================================\n" +
                "Welcome to Kniffeliger TestDemo.\n" +
                "======================================================================\n" +
                "You can use the following commands:\n" +
                "\\changeUsername <new username> to change your username\n" +
                "\\chat <message> to send a chat message to all other players\n" +
                "\\whisper <username> <message> to send a chat to only one other player\n" +
                "\\quit to leave the game\n" +
                "======================================================================\n";
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
