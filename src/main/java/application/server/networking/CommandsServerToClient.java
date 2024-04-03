package application.server.networking;

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
     * Returns the rolled dice to the player
     */
    DICE,

    /**
     * Returns a list of all lobbies to the client
     */
    LOLI,

    /**
     * Sends the current entry sheet to the player to print out in the console
     */
    SHES,

    /**
     * Gives a list of special action the player has to the client to print out
     */
    SHAC

}
