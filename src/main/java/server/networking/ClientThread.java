package server.networking;
import server.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread implements Runnable{

    private static ArrayList<Player> playerList = new ArrayList<>();
    private Player player;
    private Socket socket;

    private NetworkManagerServer networkManager;
    private ServerInput serverInput;

    public ClientThread(Socket socket) {
        this.player = new Player();
        playerList.add(player);
        this.socket = socket;
    }

    @Override
    public void run() {
        networkManager = new NetworkManagerServer(socket);
        serverInput = new ServerInput(socket, this);
        Thread thread = new Thread(serverInput);
        thread.start();

        // connection
        String msg = "Connection with " + player.getUsername() + " established.";
        System.out.println(msg);

        //networkManager.send(CommandsServerToClient.PRNT, "Alfred: " + msg);

    }

    public synchronized void changePlayerName(String username) {
        player.setUsername(username);
        networkManager.send(CommandsServerToClient.CHNA, username);
    }

    public NetworkManagerServer getNetworkManager() {
        return networkManager;
    }
}
