package server;

import java.util.ArrayList;
import server.networking.CommandsServerToClient;
import server.networking.ServerOutput;

public class Lobby {

    private String name;

    private int numbOfPlayers = 0;

    /**
     * Indicates whether a lobby is open to new players, already full or even has an ongoing game.
     * Players can only join if the lobby is open and no game is running.
     * The variable can only be "open", "full" or "ongoing game".
     */
    private String status;

    private ArrayList<Player> playersInLobby = new ArrayList<>();

    //private boolean gameIsRunning = false; needed?

    public Lobby(String name) {
        this.name = name;
        this.status = "open";
    }

    public void enterLobby(Player player) {

        ServerOutput serverOutput = player.getPlayerThreadManager().getServerOutput(); //I know this is ugly, fix later

        if(player.getLobby() != null)  {
            serverOutput.send(CommandsServerToClient.BRCT, "You can not be in two lobbies at the same time");
            return;
        }

        for (Player playerInList : playersInLobby) {
            if (playerInList.equals(player)) {
                serverOutput.send(CommandsServerToClient.BRCT, "You are already in this lobby");
                return;
            }
        }

        if (status.equals("full")) {
            serverOutput.send(CommandsServerToClient.BRCT, "Lobby " + name + " is already full");
        } else if (status.equals("ongoing game")) {
            serverOutput.send(CommandsServerToClient.BRCT, "You can not enter a Lobby when a game is running!");
        } else {
            numbOfPlayers++;
            playersInLobby.add(player);
            player.setLobby(this);
            if (numbOfPlayers == 4) {
                status = "full";
            }
            serverOutput.send(CommandsServerToClient.BRCT, "You successfully entered the lobby " + name);
        }
    }

    public void startGame(Player player) {
        ServerOutput serverOutput = player.getPlayerThreadManager().getServerOutput(); //I know this is ugly, fix later
        if (!playersInLobby.contains(player)) {
            serverOutput.send(CommandsServerToClient.BRCT, "You are not in this lobby, please enter before starting a game!");
        } else if (numbOfPlayers < 2) {
            serverOutput.send(CommandsServerToClient.BRCT, "There are not enough players in this lobby to start a game");
        } else {
            status = "ongoing game";
            // TODO how to handle starting a game in the GameManger?
        }
    }

    public void leaveLobby(Player player) {
        ServerOutput serverOutput = player.getPlayerThreadManager().getServerOutput(); //I know this is ugly, fix later

        if(!player.getLobby().equals(this)) {
            serverOutput.send(CommandsServerToClient.BRCT, "You are not in this lobby");
            return;
        }

        if (!status.equals("ongoing game")) {
            playersInLobby.remove(player);
            numbOfPlayers--;
            player.setLobby(null);
            serverOutput.send(CommandsServerToClient.BRCT, "You successfully left the lobby " + name);
            if (status.equals("full")) {
                status = "open";
            }
        }
        //TODO how to handle leaving a lobby when a game is running?
    }

    //TODO should you be able to rename a lobby?

    public ArrayList<Player> getPlayersInLobby() {
        return playersInLobby;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
