package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.logging.log4j.Logger;
import starter.Starter;

/**
 * This is the main class for the server. It contains a list of all connected clients. When it is constucted,
 * it waits for new connections and starts a new object Player every time.
 */
public class Server {

    private Logger logger = Starter.getLogger();

    private ServerSocket serverSocket;

    /**
     * Waits for new connections and constructs a new Player object.
     * @param port the port on which the connection is established
     */
    public Server(int port) {
        try {
            // establish connection
            serverSocket = new ServerSocket(port);

            // connection established
            logger.info("Waiting for connection on " + port);
            System.out.println("Waiting for connection on " + port);

            while(true) {
                // wait for connections and create new player
                Socket clientSocket = serverSocket.accept();
                Player player = new Player(clientSocket, ListManager.getPlayerList());
                ListManager.addPlayer(player);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
