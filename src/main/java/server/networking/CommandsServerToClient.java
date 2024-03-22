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
    CHAT

}
