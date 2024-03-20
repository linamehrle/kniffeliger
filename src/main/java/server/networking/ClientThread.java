package server.networking;

import server.Player;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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
        String msg = "Connection with " + player.getUsername() + " established";
        System.out.println(msg);

        //serverOutput.send(CommandsServerToClient.PRNT, "Alfred: " + msg);

        ping = new Ping(this);
        Thread pingThread = new Thread(ping);
        pingThread.start();
    }

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
            ArrayList<Player> playerList = player.getPlayerList();
            playerList.remove(player);
            ping.stop();
            serverInput.stop();
            socket.close();

            System.out.println(player.getUsername() + " has successfully disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
