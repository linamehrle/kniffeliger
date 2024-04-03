package server.networking;

import server.Player;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class handles all threads needed by a player. It contains the output stream to the client, the input thread from
 * the client and a separate thread for the ping.
 */
public class ClientThread implements Runnable{

    private Player player;
    private Socket socket;

    private ServerOutput serverOutput;
    private ServerInput serverInput;
    private Ping ping;

    /**
     * The constructor for the ClientThread.
     * @param socket
     * @param player
     */
    public ClientThread(Socket socket, Player player) {
        this.player = player;
        this.socket = socket;
    }

    /**
     * The run method for the thread. It creates a new ServerInput, ServerOutput and Ping.
     */
    @Override
    public void run() {
        serverOutput = new ServerOutput(socket);

        serverInput = new ServerInput(this);
        Thread thread = new Thread(serverInput);
        thread.start();

        // connection
        System.out.println("Connection with " + player.getUsername() + " established");

        serverOutput.send(CommandsServerToClient.BRCT, "Connection to server established");
        Communication.broadcast(player.getPlayerList(), player, "Player " + player.getUsername() + " connected to the server");

        ping = new Ping(this);
        Thread pingThread = new Thread(ping);
        pingThread.start();
    }

    /**
     * This method is used to send a message to the ServerOutput to pass along to the Client via the network protocol.
     * @param cmd
     * @param message
     */
    public void sendToServerOutput(CommandsServerToClient cmd, String message) {
        serverOutput.send(cmd, message);
    }

    /**
     * Getter for the ServerOutput.
     * @return
     */
    public ServerOutput getServerOutput() {
        return serverOutput;
    }

    /**
     * Getter for the Player that contains the threads.
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter for the Ping.
     * @return
     */
    public Ping getPing() {
        return ping;
    }

    /**
     * Getter for the socket.
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * This method handles the disconnect of a client on the server side. It closes all threads, removes the player
     * from the player list and informs all other players of the disconnect.
     */
    public void disconnect() {
        try {
            serverOutput.send(CommandsServerToClient.QUIT, "goodbye client");
            Communication.broadcast(player.getPlayerList(), player, "Player " + player.getUsername() + " has disconnected");
            ArrayList<Player> playerList = player.getPlayerList();
            playerList.remove(player);
            ping.stop();
            serverInput.stop();
            serverOutput.stop();
            socket.close();

            System.out.println(player.getUsername() + " has successfully disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
