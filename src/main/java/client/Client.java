package client;

import client.gui.Main;
import client.networking.ClientInput;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;
import client.networking.ConsoleInput;
import client.networking.Pong;
import org.apache.logging.log4j.Logger;
import starter.Starter;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Application;

/**
 * This is the main class for the client. It contains the input thread for the console and from the server and the
 * output stream to the server as well as a separate thread for the ping.
 * The constructor starts all necessary threads and prints a welcome message in the console for the user.
 * The class also contains a disconnect method which closes all threads when the client disconnects.
 */
public class Client {
    Logger logger = Starter.getLogger();

    private Socket socket;
    private ConsoleInput consoleInput;
    private ClientInput clientInput;
    private ClientOutput clientOutput;
    private Pong pong;

    /**
     * Constructor for the GameManager
     * @param hostName
     * @param port
     */
    public Client(String hostName, int port, String username) {

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

        } catch (IOException e) {
            logger.info("no server found, goodbye!");
            logger.info(e.getMessage());
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
                "\\showPlayers to et a list of all connected players\n" +
                "\\showHighScores to get a high score list\n" +
                "\\enterLobby <name> to enter a lobby of a given name\n" +
                "\\start to start a game in a lobby\n" +
                "\\gameAction to enter all commands belonging to the game\n" +
                "\\quit to leave the game\n" +
                "======================================================================\n";
        System.out.println(welcomeText);

        if (username.equals("default")) {
            String systemUsername = System.getProperty("user.name");
            clientOutput.send(CommandsClientToServer.CHNA, systemUsername);
        } else {
            clientOutput.send(CommandsClientToServer.CHNA, username);
        }

        //start the gui
        logger.debug("before main Launch");
        Application.launch(Main.class);
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
            //TODO how to stop the gui thread
            //Platform.exit();
            System.out.println("Goodbye!");
        } catch (IOException e) {
            logger.warn(e.getMessage());
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
