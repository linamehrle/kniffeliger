package server.networking;
import server.Chat;
import server.Player;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread implements Runnable{

    private static ArrayList<Player> playerList = new ArrayList<>();
    private static Chat chat = new Chat(playerList);
    private Player player;
    private Socket socket;

    private ServerOutput serverOutput;
    private ServerInput serverInput;
    private Ping ping;

    public ClientThread(Socket socket) {
        this.player = new Player();
        playerList.add(player);
        this.socket = socket;
    }

    @Override
    public void run() {
        //warum muss der output kein thread sein?
        serverOutput = new ServerOutput(socket);

        //getter für den socket? nur this übergeben?
        serverInput = new ServerInput(socket, this);
        Thread thread = new Thread(serverInput);
        thread.start();

        // connection
        String msg = "Connection with " + player.getUsername() + " established.";
        System.out.println(msg);

        //serverOutput.send(CommandsServerToClient.PRNT, "Alfred: " + msg);

        ping = new Ping(this);
        Thread pingThread = new Thread(ping);
        pingThread.start();
    }

    //in den player verlegen?
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

        player.setUsername(username);
        serverOutput.send(CommandsServerToClient.CHNA, username);
    }

    //in den player verlegen?
    private synchronized boolean usernameIsTaken(String username) {
        for (Player player : playerList) {
            if (player.getUsername().equals(username) && !player.equals(this.player)) {
                return true;
            }
        }

        return false;
    }

    public ServerOutput getServerOutput() {
        return serverOutput;
    }

    public Player getPlayer() {
        return player;
    }

    public Ping getPing() {
        return ping;
    }

    public void disconnect() {
        try {
            serverOutput.send(CommandsServerToClient.QUIT, "goodbye client");
            playerList.remove(player);
            ping.stop();
            serverInput.stop();
            socket.close();

            System.out.println("Client has successfully disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
