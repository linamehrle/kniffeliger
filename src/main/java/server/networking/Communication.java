package server.networking;

import java.util.ArrayList;

import org.apache.logging.log4j.Logger;
import server.Lobby;
import server.Player;
import starter.Starter;

/**
 * This class contains all necessary methods for communication between clients and from server to client.
 */
public class Communication {

    static Logger logger = Starter.logger;

    /**
     * sends a message from the server to all connected clients
     * @param message
     */
    public static void broadcastToAll(CommandsServerToClient cmd, ArrayList<Player> playerList, String message) {
        for (Player player : playerList) {
            ServerOutput serverOutput = player.getPlayerThreadManager().getServerOutput();
            serverOutput.send(cmd, message);
        }
    }

    /**
     * sends a message from the server to all clients in the list but the single specified player
     * @param player
     * @param message
     */
    public static void broadcast(ArrayList<Player> listOfPlayers, Player player, String message) {
        for (Player playerInList : listOfPlayers) {
            if (!playerInList.equals(player)) {
                ServerOutput serverOutput = playerInList.getPlayerThreadManager().getServerOutput(); //I know this is ugly, fix later
                serverOutput.send(CommandsServerToClient.BRCT, message);
            }
        }
    }

    /**
     * Sends a message from the server to the specified player
     * @param player
     * @param message
     */
    public static void sendToPlayer(CommandsServerToClient cmd, Player player, String message) {
        ServerOutput serverOutput = player.getPlayerThreadManager().getServerOutput();
        serverOutput.send(cmd, message);
    }

    /**
     * sends a chat message to all other clients
     * @param player the player which is sending the message
     * @param message
     */
    public static void sendChat(Player player, String message) {
        ArrayList<Player> playerList = player.getPlayerList();
        for (Player playerInList : playerList) {
            if (!playerInList.equals(player)) {
                ServerOutput serverOutput = playerInList.getPlayerThreadManager().getServerOutput(); //I know this is ugly, fix later
                serverOutput.send(CommandsServerToClient.CHAT, player.getUsername() + ": " + message);
            }
        }
    }

    /**
     * sends a chat message to only one other player
     * @param player the player which is sending the message
     * @param message contains the name of the recipient and the message to be sent
     */
    public static void sendWhisper(Player player, String message) {
        ArrayList<Player> playerList = player.getPlayerList();

        ServerOutput serverOutputSender = player.getPlayerThreadManager().getServerOutput(); //I know this is ugly, fix later

        String[] input = message.split(" ", 2);

        Player recipient = null;

        for (Player playerInList : playerList) {
            if (input[0].equals(playerInList.getUsername())) {
                recipient = playerInList;
                break;
            }
        }

        if (recipient == null) {
            serverOutputSender.send(CommandsServerToClient.BRCT, "There is no player with this name");
            return;
        }

        if (input.length < 2) {
            serverOutputSender.send(CommandsServerToClient.BRCT, "Empty message: please try again.");
        }

        ServerOutput serverOutputRecipient = recipient.getPlayerThreadManager().getServerOutput();
        serverOutputRecipient.send(CommandsServerToClient.CHAT, player.getUsername() + " whispered: " + input[1]);
    }

    /**
     * sends a chat message to all other players in the same lobby
     * @param player the sender
     * @param message
     */
    public static void sendToLobby(CommandsServerToClient cmd, Player player, String message) {

        logger.debug("sendToLobby received a message from player " + player.getUsername());

        //checks if a player is in a lobby
        if(player.getLobby() == null) {
            ServerOutput serverOutput = player.getPlayerThreadManager().getServerOutput(); //I know this is ugly, fix later
            serverOutput.send(CommandsServerToClient.BRCT, "You are not in a lobby");
        }

        //sends the message to all other players in the lobby
        Lobby lobby = player.getLobby();
        ArrayList<Player> playersInLobby = lobby.getPlayersInLobby();
        for (Player playerInList : playersInLobby) {
            if (!playerInList.equals(player)) {
                switch (cmd) {
                    case GAME -> {
                        ServerOutput serverOutput = playerInList.getPlayerThreadManager().getServerOutput(); //I know this is ugly, fix later
                        serverOutput.send(cmd, message);
                    }
                    case CHAT -> {
                        ServerOutput serverOutput = playerInList.getPlayerThreadManager().getServerOutput(); //I know this is ugly, fix later
                        serverOutput.send(CommandsServerToClient.CHAT, player.getUsername() + " to Lobby : " + message);
                        logger.debug("chat send to player " + player.getUsername());
                    }
                }
            }
        }

    }

}

