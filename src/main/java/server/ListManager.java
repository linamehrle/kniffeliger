package server;

import java.util.ArrayList;
import server.networking.CommandsServerToClient;
import server.networking.Communication;
import server.networking.ServerOutput;

/**
 * This class manages the list of players, lobbies, games etc.
 */
public class ListManager {

    private static ArrayList<Player> playerList = new ArrayList<>();

    private static ArrayList<Lobby> lobbyList = new ArrayList<>();

    //useful methods for the player list:

    public static void addPlayer(Player player) {
        playerList.add(player);
    }

    public static ArrayList<Player> getPlayerList() {
        return playerList;
    }


    //useful methods for the lobby list:


    public static Lobby getLobbyByName(String name) {
        for (Lobby lobby : lobbyList) {
            if (lobby.getName().equals(name)) {
                return lobby;
            }
        }

        return new Lobby("default");
    }

    public static String returnLobbyListAsString() {
        String output = "";
        for (Lobby lobby : lobbyList) {
            output = output + lobby.getName() + "(" + lobby.getStatus() + ") ";
        }
        return output;
    }

    public static boolean lobbyNameIsTaken(String name) {
        for (Lobby lobby : lobbyList) {
            if (lobby.getName().equals(name)) {
                return true;
            }
        }
        return false;
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

    public static void createNewLobby(Player player, String name) {
        ServerOutput serverOutput = player.getPlayerThreadManager().getServerOutput();
        if (lobbyNameIsTaken(name)) {
            serverOutput.send(CommandsServerToClient.BRCT, "Name is already taken");
        } else {
            getLobbyList().add(new Lobby(name));
            System.out.println("Player " + player.getUsername() + " created a new lobby: " + name);
            serverOutput.send(CommandsServerToClient.BRCT, "You successfully created the lobby " + name); //to the player
            Communication.broadcast(player, "Player " + player.getUsername() + " created a new lobby " + name); //to all other players
        }
    }

    public static ArrayList<Lobby> getLobbyList() {
        return lobbyList;
    }
}
