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

    //TODO listen in eigene Klasse?
    private static ArrayList<Player> playerList = new ArrayList<>();

    private static ArrayList<Lobby> lobbyList = new ArrayList<>();

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

    public static String returnLobbyList() {
        String output = "";
        for (Lobby lobby : lobbyList) {
            output = output + lobby.getName() + "(" + lobby.getStatus() + ") ";
        }
        return output;
    }

    public static boolean lobbyNameIsTaken(String name) {
        for (Lobby lobby : lobbyList) {
            if (lobby.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a lobby with a given name exists in the list of all lobbies
     * @param name
     * @return
     */
    public static boolean lobbyExists(String name) {
        for (Lobby lobby : lobbyList) {
            if (lobby.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a lobby from the list of all lobbies given the unique name of the lobby
     * @param name
     * @return
     */
    public static Lobby getLobbyFromList(String name) {
        for (Lobby lobbyInList : lobbyList) {
            if (lobbyInList.getName().equals(name)) {
                return lobbyInList;
            }
        }
        Lobby lobby = new Lobby("default");
        return lobby;
    }

    public static ArrayList<Lobby> getLobbyList() {
        return lobbyList;
    }
}
