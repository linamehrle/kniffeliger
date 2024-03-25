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
     *  Communicates to the server that a player wants to roll all dice that are not saved
     */
    ROLL,

    /**
     * used to save certain dice, so they are not rerolled in the next roll
     */
    SAVE

}
