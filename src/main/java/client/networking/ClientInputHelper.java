package client.networking;

import client.manager.GameManager;
import server.networking.CommandsServerToClient;

/**
 * This class handles the input read by the ClientInput class and processes it accordingly.
 * This class is a thread.
 */
public class ClientInputHelper implements Runnable {
    GameManager gameManager;
    String message;
    Pong pong;
    ClientOutput clientOutput;

    /**
     * The constructor for the ClientInputHelper
     * @param gameManager
     * @param message the input that was read by the ClientInput coming from the server
     */
    public ClientInputHelper(GameManager gameManager, String message) {
        this.gameManager = gameManager;
        this.message = message;
        this.pong = gameManager.getPong();
        this.clientOutput = gameManager.getClientOutput();
    }

    /**
     * The run method for the ClientInputHelper. It splits the incoming string according to the network protocol
     * and handles the different cases accordingly.
     */
    @Override
    public void run() {

        String[] input = message.split(" ", 2);
        CommandsServerToClient cmd;

        try {
            cmd = CommandsServerToClient.valueOf(input[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Client: received invalid command " + input[0]);
            return;
        }

        //switch case for the different possible incoming commands
        switch (cmd) {

            case CHNA -> System.out.println("Your username is now " + input[1]);
            case QUIT -> gameManager.disconnect();
            case PING -> clientOutput.send(CommandsClientToServer.PONG, input[1]);
            case PONG -> pong.updatePong(input[1]);
            case CHAT -> System.out.println(input[1]);
            case BRCT-> System.out.println("Alfred: " + input[1]);
            default -> System.out.println("unknown command received from server " + message);
        }

    }
}
