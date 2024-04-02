package server;

import java.util.ArrayList;
import server.networking.CommandsServerToClient;
import server.networking.Communication;
import server.networking.ServerOutput;

/**
 * This class manages the list of players, lobbies, games etc.
 */
public class ListManager {

    /**
     * List of all players currently connected to the server
     */
    private static ArrayList<Player> playerList = new ArrayList<>();

    /**
     * List of all existing lobbies
     */
    private static ArrayList<Lobby> lobbyList = new ArrayList<>();


    //useful methods for the player list:

    /**
     * Adds a new player to the list of players
     * @param player
     */
    public static void addPlayer(Player player) {
        playerList.add(player);
    }

    /**
     * Getter for the list of all players
     * @return
     */
    public static ArrayList<Player> getPlayerList() {
        return playerList;
    }


    //useful methods for the lobby list:

    /**
     * Returns a lobby by identifying its unique name
     * @param name
     * @return
     */
    public static Lobby getLobbyByName(String name) {
        for (Lobby lobby : lobbyList) {
            if (lobby.getName().equals(name)) {
                return lobby;
            }
        }

        return new Lobby("default");
    }

    /**
     * Returns a string with all existing lobbies and their current status in the form of
     * "lobby(status) lobby(status) ..."
     * @return
     */
    public static String returnLobbyListAsString() {
        String output = "";
        for (Lobby lobby : lobbyList) {
            output = output + lobby.getName() + "(" + lobby.getStatus() + ") ";
        }
        return output;
    }

    /**
     * Checks if a lobby with a given name exists in the list of all lobbies
     * @param name
     * @return
     */
    public static boolean lobbyExists(String name) {
        for (Lobby lobby : lobbyList) {
            if (lobby.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new lobby with a given name if no lobby with this name exists
     * @param player the player who creates the lobby
     * @param name
     */
    public static void createNewLobby(Player player, String name) {
        ServerOutput serverOutput = player.getPlayerThreadManager().getServerOutput();
        if (lobbyExists(name)) {
            serverOutput.send(CommandsServerToClient.BRCT, "Name is already taken");
        } else {
            getLobbyList().add(new Lobby(name));
            System.out.println("Player " + player.getUsername() + " created a new lobby: " + name);
            serverOutput.send(CommandsServerToClient.BRCT, "You successfully created the lobby " + name); //to the player
            Communication.broadcast(player, "Player " + player.getUsername() + " created a new lobby " + name); //to all other players
        }
    }

    /**
     * Getter for the list of existing lobbies
     * @return
     */
    public static ArrayList<Lobby> getLobbyList() {
        return lobbyList;
    }
}
