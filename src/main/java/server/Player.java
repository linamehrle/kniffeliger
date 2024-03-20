package server;

import java.net.Socket;
import java.util.ArrayList;
import server.networking.ClientThread;
import server.networking.CommandsServerToClient;

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

        System.out.println("received the username: " + username);

        username = username.replace(" ", "_");

        if(usernameIsTaken(username)) {
            int counter = 1;

            while (usernameIsTaken(username + "_" + counter)) {
                counter++;
            }

            username = username + "_" + counter;
        }

        setUsername(username);
        playerThreadManager.sendToServerOutput(CommandsServerToClient.CHNA, username);
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

    //more getter and setter here
}
