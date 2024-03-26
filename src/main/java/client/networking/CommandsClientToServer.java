package client.networking;

/**
 * contains all commands the client can send to the server
 */
public enum CommandsClientToServer {

    /**
     * sends a message to be redistributed to all other clients. The message si shown as a chat in the form
     * sender: message
     */
    CHAT,

    /**
     *  sends a message and a recipient to the server, only the given client receives the message in the form
     *  sender whispered: message
     */
    WHSP,

    /**
     *  used to change the username of a client, sends the new username to the server, server changes the saved username
     *  and informs all connected clients of this change appropriately
     */
    CHNA,

    /**
     * indicates that a ping is sent to the server, the message contains the system time
     */
    PING,

    /**
     *  indicates a ping that has been returned by the server
     */
    PONG,

    /**
     *  used to disconnect a client, send message to server that a client has disconnected
     */
    QUIT,

    /**
     * Requests a list of all existing lobbies from the server to print in the console
     */
    LOLI,

    /**
     * Used to create a new Lobby with a given name, name must be unique
     */
    CRLO,

    /**
     * Used ba the client to enter a lobby of a given name
     */
    ENLO,

    /**
     *  Used by the client to leave a Lobby of a given name
     */
    LELO

}
