package server.networking;

/**
 * contains all commands the server can send to the client
 */
public enum CommandsServerToClient {

    /**
     *  Used to send an internal message from the server to the client. The message will be shown to the client as
     *  Alfred: message
     */
    BRCT,

    /**
     *  Used to confirm name change to client
     */
    CHNA,

    /**
     * Used to send a ping to check for connection losses
     */
    PING,

    /**
     * Used to return a ping coming from the client.
     */
    PONG,

    /**
     * Used to initiate the disconnect of a client.
     */
    QUIT,

    /**
     * Used to send chat messages to other clients.
     */
    CHAT,

    /**
     * Returns a list of all lobbies to the client
     */
    LOLI,

    /**
     * Used to tell the gui that a new lobby has been created
     */
    CRLO,

    /**
     * Used to tell the gui that a player has entered a lobby
     */
    ENLO,

    /**
     * Used to tell the gui that a player has left a lobby
     */
    LELO,

    /**
     * Returns a list with all connected players to the client
     */
    PLLI,

    /**
     * Requests a high score list from the server
     */
    HGSC,


    /**
     * Sends rolled dice to the client
     */
    ROLL,

    /**
     * Sends an update of a lobby status to the client to update the gui
     */
    LOST,

    /**
     * Starts a turn and communicates the player name and the game phase
     */
    STRT,

    /**
     * Communicates to the gui that a player has stolen an entry of another player
     */
    STEA,

    /**
     * Communicates to the gui that a player has frozen an entry of another player
     */
    FRZE,

    /**
     * Communicates to the gui that a player has crossed out an entry of another player
     */
    COUT,

    /**
     * Communicates the updated entry to the client to display in the gui
     */
    ENTY,

    /**
     * Communicates a shift of entry sheets to the gui
     */
    SHFT,

    /**
     * Communicates a swap of entry sheets to the gui
     */
    SWAP,

    /**
     * Communicates that a player has gotten an action die
     */
    ACTN,

    /**
     * Communicates the final score of the game to display
     */
    RANK,

    /**
     * Returns a list of all players that are in a certain lobby
     */
    LOPL,

    /**
     * Updates tab 2 of GUI (non-active players) with result of ROLL (dice values)
     */
    ALDI,

    /**
     * Updates entries in tab 2 of GUI (non-active players)
     */
    ALES


}
