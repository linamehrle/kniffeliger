package server;

import server.networking.NetworkManagerServer;

/**
 * This class represents the server and is designed to handle communications between the single lobbies, clients and logic.
 */
public class Server {
    NetworkManagerServer networkManager;

    public Server(int port) {
        networkManager = new NetworkManagerServer(port);
    }
}
