package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This is the main class for the server. It contains a list of all connected clients. When it is constucted,
 * it waits for new connections and starts a new object Player every time.
 */
public class Server {

    private ServerSocket serverSocket;

    private static ArrayList<Player> playerList = new ArrayList<>();

    /**
     * Waits for new connections and constructs a new Player object.
     * @param port the port on which the connection is established
     */
    public Server(int port) {
        try {
            // establish connection
            serverSocket = new ServerSocket(port);

            // connection established
            System.out.println("Waiting for connection on " + port);

            while(true) {
                // wait for connections and create new player
                Socket clientSocket = serverSocket.accept();
                Player player = new Player(clientSocket, playerList);
                playerList.add(player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
