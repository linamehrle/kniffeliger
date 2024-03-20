package server.networking;

import server.Player;
import java.io.IOException;
import java.net.Socket;

public class ClientThread implements Runnable{

    private Player player;
    private Socket socket;

    private ServerOutput serverOutput;
    private ServerInput serverInput;
    private Ping ping;

    public ClientThread(Socket socket, Player player) {
        this.player = player;
        this.socket = socket;
    }

    @Override
    public void run() {
        //warum muss der output kein thread sein?
        serverOutput = new ServerOutput(socket);

        //getter für den socket? nur this übergeben?
        serverInput = new ServerInput(this);
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
    /*public synchronized void changePlayerName(String username) {

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
    }*/

    public void sendToServerOutput(CommandsServerToClient cmd, String message) {
        serverOutput.send(cmd, message);
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

    public Socket getSocket() {
        return socket;
    }

    public void disconnect() {
        try {
            serverOutput.send(CommandsServerToClient.QUIT, "goodbye client");
            //playerList.remove(player); //how to remove player on disconnect?
            ping.stop();
            serverInput.stop();
            socket.close();

            System.out.println("Client has successfully disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
