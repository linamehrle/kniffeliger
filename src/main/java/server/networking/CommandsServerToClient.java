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
     * Used to give out game related information to the client
     */
    GAME,

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

    STEA,

    FRZE,

    COUT,

    /**
     * Communicates the updated entry to the client to display in the gui
     */
    ENTY,

    SHFT,

    SWAP,

    ACTN,

    RANK


}
