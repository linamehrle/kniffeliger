package server;

import java.util.ArrayList;

public class Chat {

    private ArrayList<Player> playerList;

    public Chat(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    /**
     * sends a message from the server to all connected clients
     * @param message
     */
    public void broadcast(String message) {
        //TODO
    }

    /**
     * sends a chat message to all other clients (later: in the same lobby?)
     * @param player the player which is sending the message
     * @param message
     */
    public void sendChat(Player player, String message) {
        //TODO
    }

    /**
     * sends a chat message to only one other player
     * @param player the player which is sending the message
     * @param message contains the name of the recipient and the message to be send
     */
    public void sendWhisper(Player player, String message) {
        //TODO
    }
}
