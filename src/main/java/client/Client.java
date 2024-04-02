package client;

import client.networking.ClientInput;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;
import client.networking.ConsoleInput;
import client.networking.Pong;
import java.io.IOException;
import java.net.Socket;

/**
 * This is the main class for the client. It contains the input thread for the console and from the server and the
 * output stream to the server as well as a separate thread for the ping.
 * The constructor starts all necessary threads and prints a welcome message in the console for the user.
 * The class also contains a disconnect method which closes all threads when the client disconnects.
 */
public class Client {

    private Socket socket;
    private ConsoleInput consoleInput;
    private ClientInput clientInput;
    private ClientOutput clientOutput;
    private Pong pong;

    //private GameLogicManager logicManager; for later

    /**
     * Constructor for the GameManager
     * @param hostName
     * @param port
     */
    public Client(String hostName, int port, String username) {

        //TODO handle username given as input parameter

        try {
            socket = new Socket(hostName, port);

            //start the output stream
            clientOutput = new ClientOutput(socket);

            //start the input thread from the console
            consoleInput = new ConsoleInput(clientOutput);
            Thread consoleThread = new Thread(consoleInput);
            consoleThread.start();

            //start the input thread from the client
            clientInput = new ClientInput(socket, this);
            Thread clientThread = new Thread(clientInput);
            clientThread.start();

            //start the thread for the ping
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
                "\\newLobby <name> to create a new lobby with the given name\n" +
                "\\showLobbies to get a list of all existing lobbies\n" +
                "\\enterLobby <name> to enter a lobby of a given name\n" +
                "\\start to start a game in a lobby\n" +
                "\\roll to roll the dice\n" +
                "\\save <numbers of the dices> to save dices before re rolling\n" +
                "\\enterCombination <name> to enter the dice in the given entry of the entry sheet\n" +
                "\\showEntrySheet or \\showEntrySheet <name> to see you own or another players entry sheet\n" +
                "\\showActions to see your special actions\n" +
                "\\playAction <name> to play a special action\n" +
                "\\quit to leave the game\n" +
                "======================================================================\n";
        System.out.println(welcomeText);

        if (username.equals("default")) {
            String systemUsername = System.getProperty("user.name");
            clientOutput.send(CommandsClientToServer.CHNA, systemUsername);
        } else {
            clientOutput.send(CommandsClientToServer.CHNA, username);
        }
        // start game
        this.start();
    }

    /**
     * This function starts the game.
     */
    private void start() {
        //do we need this later?
    }

    /**
     * This method handles the disconnect of the client.
     */
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

    /**
     * Getter for the output thread to the server.
     * @return
     */
    public ClientOutput getClientOutput() {
        return clientOutput;
    }

    /**
     * Getter for the ping Thread.
     * @return
     */
    public Pong getPong() {
        return pong;
    }
}
