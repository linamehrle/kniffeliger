package server;

import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import server.gamelogic.EntrySheet;
import server.gamelogic.GameManager;
import server.networking.CommandsServerToClient;
import server.networking.Communication;
import starter.Starter;

/**
 * This class represents a lobby in the game
 */
public class Lobby {

    /**
     * The name of the lobby, it is unique
     */
    private String name;

    private int numbOfPlayers = 0;

    /**
     * Game Manager used for lobby.
     */
    private GameManager gameManager;

    /**
     * Indicates whether a lobby is open to new players, already full or even has an ongoing game.
     * Players can only join if the lobby is open and no game is running.
     * The variable can only be "open", "full" or "ongoing game".
     */
    private String status;

    private ArrayList<Player> playersInLobby = new ArrayList<>();

    private EntrySheet[] entrySheets;

    private EntrySheet currentEntrySheet;
    private Logger logger = Starter.getLogger();

    //private boolean gameIsRunning = false; needed?

    /**
     * Constructor for the lobby, the initial status is open
     * @param name
     */
    public Lobby(String name) {
        //name.replaceAll(" ", "_");
        this.name = name;
        this.status = "open";
        this.gameManager = new GameManager();
    }

    /**
     * Used by a player to enter a lobby. This is only possible if the status is open, not if the lobby is full
     * or a game is currently running
     * @param player
     */
    public void enterLobby(Player player) {

        if (player.getLobby() != null) {
            if (player.getLobby().equals(this)) {
                Communication.sendToPlayer(CommandsServerToClient.BRCT, player, "You are already in this lobby");
                return;
            } else {
                Communication.sendToPlayer(CommandsServerToClient.BRCT, player, "You can not be in two lobbies at the same time");
                return;
            }
        }

        if (status.equals("full")) {
            Communication.sendToPlayer(CommandsServerToClient.BRCT, player, "Lobby " + name + " is already full");
        } else if (status.equals("ongoing game")) {
            Communication.sendToPlayer(CommandsServerToClient.BRCT, player, "You can not enter a Lobby when a game is running!");
        } else {
            numbOfPlayers++;
            playersInLobby.add(player);
            player.setLobby(this);
            if (numbOfPlayers == 4) {
                status = "full";
                Communication.broadcastToAll(CommandsServerToClient.LOST, ListManager.getPlayerList(), name + " (full)");
            }
            Communication.sendToPlayer(CommandsServerToClient.BRCT, player, "You successfully entered the lobby " + name);
            Communication.broadcastToAll(CommandsServerToClient.LOPL, playersInLobby, getPlayersInLobbyAsString());
        }
    }

    public void prepareForGame(Player player) {
        logger.debug("TRYING TO PREPARE FOR THE GAME");

        if (!playersInLobby.contains(player)) {
            Communication.sendToPlayer(CommandsServerToClient.BRCT, player, "You are not in this lobby, please enter before starting a game!");
        } else if (numbOfPlayers < 2) {
            Communication.sendToPlayer(CommandsServerToClient.BRCT, player, "There are not enough players in this lobby to start a game");
        } else {
            // sets the player in the lobby
            //gameManager.setPlayers(playersInLobby);
            gameManager.prepareForStart(this);
        }
    }

    /**
     * This method starts the game if enough players are in the lobby. Only a player that is in the lobby can start a
     * game in the lobby
     * @param player
     */
    public void startGame(Player player) {
        logger.debug("TRYING TO START GAME");

        if (status.equals("ongoing game")) {
            logger.info("Game is already running");
            return;
        }

        if (!playersInLobby.contains(player)) {
            Communication.sendToPlayer(CommandsServerToClient.BRCT, player, "You are not in this lobby, please enter before starting a game!");
        } else if (numbOfPlayers < 2) {
            Communication.sendToPlayer(CommandsServerToClient.BRCT, player, "There are not enough players in this lobby to start a game");
        } else {
            status = "ongoing game";
            Communication.broadcastToAll(CommandsServerToClient.LOST, ListManager.getPlayerList(), name + " (ongoing game)");

            // starts the game
            prepareForGame();
            Thread gameThread = new Thread(gameManager);
            gameThread.start();
        }
    }

    /**
     * Used by a player to leave the lobby.
     * @param player
     */
    public void leaveLobby(Player player) {

        if(!player.getLobby().equals(this)) {
            Communication.sendToPlayer(CommandsServerToClient.BRCT, player, "You are not in this lobby");
            return;
        }

        if (!status.equals("ongoing game")) {
            numbOfPlayers--;
            Communication.sendToPlayer(CommandsServerToClient.BRCT, player, "You successfully left the lobby " + name);
            Communication.broadcastToAll(CommandsServerToClient.LELO, ListManager.getPlayerList(),
                    player.getLobby().getName() + " ("+ player.getLobby().getStatus() + "):" + player.getUsername());
            playersInLobby.remove(player);
            player.setLobby(null);
            if (status.equals("full")) {
                status = "open";
                Communication.broadcastToAll(CommandsServerToClient.LOST, ListManager.getPlayerList(), name + " (open)");
            }
        }
        Communication.broadcastToAll(CommandsServerToClient.LOPL, playersInLobby, getPlayersInLobbyAsString());
        //TODO how to handle leaving a lobby when a game is running?
    }


    /**
     * Handles the lobby status at the end of the game
     */
    public void gameEnded() {
        if(playersInLobby.size() == 4) {
            status = "full";
            Communication.broadcastToAll(CommandsServerToClient.LOST, ListManager.getPlayerList(), name + " (full)");
        } else {
            status = "open";
            Communication.broadcastToAll(CommandsServerToClient.LOST, ListManager.getPlayerList(), name + " (open)");
        }
    }

    /**
     * Returns a list of the players in the lobby as a string in the following way: "username,username,..."
     * @return
     */
    public String getPlayersInLobbyAsString() {
        String playersInLobbyAsString = "";
        for (Player player : playersInLobby) {
            playersInLobbyAsString = playersInLobbyAsString + player.getUsername() + ",";
        }
        return playersInLobbyAsString;
    }

    public void prepareForGame() {
        entrySheets = new EntrySheet[playersInLobby.size()];
        for (int i = 0; i < playersInLobby.size(); i++) {
            entrySheets[i] = new EntrySheet(playersInLobby.get(i));
        }
    }

    /**
     * Getter for the list of players that are in the lobby
     * @return
     */
    public ArrayList<Player> getPlayersInLobby() {
        return playersInLobby;
    }

    /**
     * Getter for the name of the lobby
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the status of the lobby
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * Getter for the Game Manager
     * @return
     */
    public GameManager getGameManager() {
        return gameManager;
    }

    public synchronized EntrySheet[] getEntrySheets() {
        return entrySheets;
    }

    public synchronized void setEntrySheets(EntrySheet[] entrySheets) {
        this.entrySheets = entrySheets;
    }

    public synchronized EntrySheet getCurrentEntrySheet() {
        return currentEntrySheet;
    }

    public synchronized void setCurrentEntrySheet(EntrySheet currentEntrySheet) {
        this.currentEntrySheet = currentEntrySheet;
    }
}
