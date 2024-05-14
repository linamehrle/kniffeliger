package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import server.gamelogic.ActionDiceEnum;
import server.networking.ClientThread;
import server.networking.CommandsServerToClient;
import server.networking.Communication;
import starter.Starter;

/**
 * This class contains all methods and information (e.g. username) about one single Player/Client that is connected
 * to the server.
 */
public class Player {

    private Logger logger = Starter.getLogger();

    /**
     * This variable counts all instances of connected players.
     */
    private static int counter = 0;
    protected int id;
    protected String username;

    /**
     * This map saves all the action dice a player gets during the game, the first field is the name of the action dice
     * and the second the current count of this specific action die
     */
    protected HashMap<ActionDiceEnum, Integer> actionDice = new HashMap<>();
    private ClientThread playerThreadManager;
    private ArrayList<Player> playerList;
    private Lobby lobby;

    /**
     * Boolean that indicates if a player is in a running game, this is needed to handle disconnects accordingly
     */
    private boolean isInGame;

    /**
     * Boolean that indicates if a player that is in a game is actually online.
     * This is needed so the game logic can "play for the player" in case of a connection loss to make
     * reconnecting possible
     */
    private boolean isOnline;

    /**
     * Counts the usages of the cheat count to punish a player that uses it more than once
     */
    private int cheatCodesUsed;

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
        isOnline = true;
        cheatCodesUsed = 0;
        playerThreadManager = new ClientThread(socket, this);
        Thread playerThread = new Thread(playerThreadManager);
        playerThread.start();
    }

    /**
     * Second constructor for the Player, used for testing
     * @param username
     */
    public Player(String username) {
        this.username = username;
        prepareForGame();
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
        //Communication.broadcast(this.getPlayerList(), this, "Player " + savedUsername + " has changed their name to " + username);
        Communication.broadcastToAll(CommandsServerToClient.PLLI, ListManager.getPlayerList(), ListManager.getPlayerListAsString());

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
    public int getActionDiceCount(ActionDiceEnum diceName) {
        return actionDice.get(diceName);
    }

    /**
     * Increases the counter of the given action ba exactly one
     * @param diceName
     */
    public void increaseActionDiceCount(ActionDiceEnum diceName) {
        int currentCount = actionDice.get(diceName);
        actionDice.replace(diceName, currentCount + 1);
    }

    /**
     * Decreases the counter of the given action ba exactly one
     * @param diceName
     */
    public void decreaseActionDiceCount(ActionDiceEnum diceName) {
        int currentCount = actionDice.get(diceName);
        actionDice.replace(diceName, currentCount - 1);
    }

    /**
     * Returns the list of action dices and how many the player has as a String
     * @return a String in the form of: "actionName:count,actionName:count,..."
     */
    public String getActionDiceAsString() {
        String actionDiceAsString = "";
        for (ActionDiceEnum actionDie : actionDice.keySet()) {
            actionDiceAsString += actionDie.toString() + ":" + actionDice.get(actionDie) + ",";
        }
        return actionDiceAsString;
    }

    /**
     * Set a new set of action dice.
     *
     * @param newActionDice new array of action dice a player can use.
     */
    public void setActionDices(HashMap<ActionDiceEnum, Integer> newActionDice) {
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

    /**
     * Prepares all necessary variables in the player fo the game, i.e. the list of action dices and the boolean isInGame
     */
    public void prepareForGame() {
        isInGame = true;
        actionDice.put(ActionDiceEnum.STEAL, 0);
        actionDice.put(ActionDiceEnum.FREEZE, 0);
        actionDice.put(ActionDiceEnum.CROSSOUT, 0);
        actionDice.put(ActionDiceEnum.SHIFT, 0);
        actionDice.put(ActionDiceEnum.SWAP, 0);
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getCheatCodesUsed() {
        return cheatCodesUsed;
    }

    public void setCheatCodesUsed(int cheatCodesUsed) {
        this.cheatCodesUsed = cheatCodesUsed;
    }
}
