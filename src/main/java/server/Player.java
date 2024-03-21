package server;

import java.net.Socket;
import java.util.ArrayList;
import server.networking.ClientThread;
import server.networking.CommandsServerToClient;
import server.networking.Communication;

public class Player {

    private static int counter = 0;
    private int id;
    private String username;
    private ClientThread playerThreadManager;
    private ArrayList<Player> playerList;

    //potenziell felder für lobbyzugehörigkeit, aktivität etc?

    public Player(Socket socket, ArrayList<Player> playerList) {
        counter++;
        this.id = counter;
        this.username = "user_" + id;
        this.playerList = playerList;
        playerThreadManager = new ClientThread(socket, this);
        Thread playerThread = new Thread(playerThreadManager);
        playerThread.start();

    }

    public synchronized void changePlayerName(String username) {
        String savedUsername = this.username;

        username = username.replace(" ", "_");

        if (usernameIsTaken(username)) {
            int counter = 1;

            while (usernameIsTaken(username + "_" + counter)) {
                counter++;
            }

            username = username + "_" + counter;
        }

        if (username == null) {
            username = "AnisjaIstDieBeste<3";
        }

        if (username == "") {
            username = "defaultName";
        }

        setUsername(username);
        playerThreadManager.sendToServerOutput(CommandsServerToClient.BRCT, "Your username is now " + username);
        Communication.broadcast(this, "Player " + savedUsername + " has changed their name to " + username);

        System.out.println("Player " + savedUsername + " has changed their name to " + username);

    }

    private synchronized boolean usernameIsTaken(String username) {
        for (Player player : playerList) {
            if (player.getUsername().equals(username) && !player.equals(this)) {
                return true;
            }
        }

        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public ClientThread getPlayerThreadManager() {
        return playerThreadManager;
    }
}
