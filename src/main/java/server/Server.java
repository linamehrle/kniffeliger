package server;

import server.networking.NetworkManagerServer;

/**
 * This class represents the server and is designed to handle communications between the single lobbies, clients and logic.
 */
public class Server {
    NetworkManagerServer networkManager;

    public static void main(String[] args) {
        new Server(Integer.parseInt(args[0]));
    }

    public Server(int port) {
        networkManager = new NetworkManagerServer(port);
    }
}
