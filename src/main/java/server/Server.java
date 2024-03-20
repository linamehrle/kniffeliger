package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ServerSocket serverSocket;

    private static ArrayList<Player> playerList = new ArrayList<>();

    public static void main(String[] args) {
        new Server(Integer.parseInt(args[0]));
    }

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
