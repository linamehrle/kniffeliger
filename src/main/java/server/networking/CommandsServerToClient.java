package server.networking;

/**
 * contains all commands the server can send to the client
 */
public enum CommandsServerToClient {

    /**
     *  sends a message for the client to print out (terminal, chat, etc.)
     */
    PRNT,

    /**
     * sends a ping to check for connection losses
     */
    PING

}
