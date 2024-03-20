package server.networking;

import java.util.ArrayList;

import server.Player;
import server.networking.CommandsServerToClient;
import server.networking.ServerOutput;

public class Communication {

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
            serverOutputSender.send(CommandsServerToClient.PRNT, "There is no player with this name");
            return;
        }

        if (input.length < 2) {
            serverOutputSender.send(CommandsServerToClient.PRNT, "Empty message: please try again.");
        }

        ServerOutput serverOutputRecipient = recipient.getPlayerThreadManager().getServerOutput();
        serverOutputRecipient.send(CommandsServerToClient.CHAT, player.getUsername() + " whispered: " + input[1]);
    }

}
