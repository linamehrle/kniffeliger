package server.networking;
import server.Player;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread implements Runnable{

    private static ArrayList<Player> playerList = new ArrayList<>();
    private Player player;
    private Socket socket;

    private ServerOutput networkManager;
    private ServerInput serverInput;

    public ClientThread(Socket socket) {
        this.player = new Player();
        playerList.add(player);
        this.socket = socket;
    }

    @Override
    public void run() {
        networkManager = new ServerOutput(socket);
        serverInput = new ServerInput(socket, this);
        Thread thread = new Thread(serverInput);
        thread.start();

        // connection
        String msg = "Connection with " + player.getUsername() + " established.";
        System.out.println(msg);

        //networkManager.send(CommandsServerToClient.PRNT, "Alfred: " + msg);

    }

    public synchronized void changePlayerName(String username) {

        username = username.replace(" ", "_");

        if(usernameIsTaken(username)) {
            int counter = 1;

            while (usernameIsTaken(username + "_" + counter)) {
                counter++;
            }

            username = username + "_" + counter;
        }

        player.setUsername(username);
        networkManager.send(CommandsServerToClient.CHNA, username);
    }

    private synchronized boolean usernameIsTaken(String username) {
        for (Player player : playerList) {
            if (player.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    public ServerOutput getNetworkManager() {
        return networkManager;
    }

    public void disconnect() {
        try {
            networkManager.send(CommandsServerToClient.QUIT, "goodbye client");
            playerList.remove(player);
            serverInput.stop();
            socket.close();

            System.out.println("Client has successfully disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
