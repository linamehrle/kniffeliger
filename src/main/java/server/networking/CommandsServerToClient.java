package server.networking;

/**
 * contains all commands the server can send to the client
 */
public enum CommandsServerToClient {

    /**
     *  sends a message for the client to print out in the terminal
     */
    BRCT,

    /**
     *  confirms name change to client
     */
    CHNA,

    /**
     * sends a ping to check for connection losses
     */
    PING,

    PONG,

    QUIT,

    CHAT

}
