package server;

import java.net.Socket;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;
import server.gamelogic.ActionDice;
import server.networking.ClientThread;
import server.networking.CommandsServerToClient;
import server.networking.Communication;
import starter.Starter;

/**
 * This class contains all methods and information (e.g. username) about one single Player/Client that is connected
 * to the server.
 */
public class Player {

    Logger logger = Starter.logger;

    /**
     * This variable counts all instances of connected players.
     */
    private static int counter = 0;
    private int id;
    private String username;
    private ActionDice[] actionDice;
    private ClientThread playerThreadManager;
    private ArrayList<Player> playerList;
    private Lobby lobby;

    //potenziell felder für aktivität etc?

    /**
     * The constructor for the Player class. It starts a new ClientThread per Player.
     * @param socket
     * @param playerList
     */
    public Player(Socket socket, ArrayList<Player> playerList) {
        counter++;
        this.id = counter;
        this.username = "user_" + id;
        this.playerList = playerList;
        playerThreadManager = new ClientThread(socket, this);
        Thread playerThread = new Thread(playerThreadManager);
        playerThread.start();

    }

    /**
     * This method handles name changes for a player. It assures that usernames are unique and informs
     * all connected players about name changes.
     * @param username
     */
    public synchronized void changePlayerName(String username) {
        String savedUsername = this.username;
        username = username.replace(" ", "_"); //usernames can not contain spaces

        //adds the next bigger number to a username if a name is already taken
        if (usernameIsTaken(username)) {
            int counter = 1;
            while (usernameIsTaken(username + "_" + counter)) {
                counter++;
            }
            username = username + "_" + counter;
        }

        if (username == "") {
            username = "defaultName";
        }

        //informs the players about the name changes.
        setUsername(username);
        playerThreadManager.sendToServerOutput(CommandsServerToClient.BRCT, "Your username is now " + username);
        Communication.broadcast(this.getPlayerList(), this, "Player " + savedUsername + " has changed their name to " + username);

        logger.info("Player " + savedUsername + " has changed their name to " + username);

    }

    /**
     * Checks whether a username is already taken using the player list from the server.
     * @param username
     * @return It returns a boolean which indicated if a username is taken or not.
     */
    private synchronized boolean usernameIsTaken(String username) {
        for (Player player : playerList) {
            if (player.getUsername().equals(username) && !player.equals(this)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Used by the player to leave the lobby they are in
     */
    public void leaveLobby() {
        lobby.leaveLobby(this);
    }

    /**
     * Used by the player to enter a lobby by giving its name. The method also checks if a lobby with the given name
     * exists.
     * @param name
     */
    public void enterLobby(String name) {
        if(ListManager.lobbyExists(name)) {
            Lobby lobbyByName = ListManager.getLobbyByName(name);
            lobbyByName.enterLobby(this);
        } else {
            playerThreadManager.getServerOutput().send(CommandsServerToClient.BRCT, "There is no lobby with this name");
        }

        Communication.broadcastToAll(CommandsServerToClient.ENLO, playerList, name + " (" +
                getLobby().getStatus() + "):" + username);
    }

    //TODO remove player from lobby when disconnecting? how to handle possible reconnect?

    /**
     * Getter for the username.
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username.
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the action dice of player.
     *
     * @return all action dice of player saved in array.
     */
    public ActionDice[] getActionDice() {
        return actionDice;
    }

    /**
     * Set a new set of action dice.
     *
     * @param newActionDice new array of action dice a player can use.
     */
    public void setActionDices(ActionDice[] newActionDice) {
        actionDice = newActionDice;
    }

    /**
     * Getter for the player list which is given by the server on construction.
     * @return
     */
    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    /**
     * Getter for the ClientThread.
     * @return
     */
    public ClientThread getPlayerThreadManager() {
        return playerThreadManager;
    }

    /**
     * Getter for the lobby
     * @return
     */
    public Lobby getLobby() {
        return lobby;
    }

    /**
     * Setter for the lobby
     * @param lobby
     */
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}
