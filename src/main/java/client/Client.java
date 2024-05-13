package client;

import client.gui.Main;
import client.networking.ClientInput;
import client.networking.ClientOutput;
import client.networking.CommandsClientToServer;
import client.networking.Pong;
import org.apache.logging.log4j.Logger;
import starter.Starter;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Application;
import javafx.application.Platform;

/**
 * This is the main class for the client. It contains the input thread for the console and from the server and the
 * output stream to the server as well as a separate thread for the ping.
 * The constructor starts all necessary threads and prints a welcome message in the console for the user.
 * The class also contains a disconnect method which closes all threads when the client disconnects.
 */
public class Client {
    Logger logger = Starter.getLogger();

    private Socket socket;
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
            clientInput.stop();
            pong.stop();
            socket.close();
            clientOutput.stop();
            Platform.exit();
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
