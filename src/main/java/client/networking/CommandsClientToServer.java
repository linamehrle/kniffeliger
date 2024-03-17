package client.networking;

/**
 * contains all commands the client can send to the server
 */
public enum CommandsClientToServer {

    /**
     * sends a message to be redistributed to all clients (and printed by them)
     */
    CHAT,

    /**
     *  sends a message and a recipient to the server, only the given client receives the message
     */
    WHSP,

    /**
     *  sends a new username to the server, server changes the saved username
     */
    CHNA,

    /**
     *  returns a ping from the server with a ping from the client
     */
    PONG

}
