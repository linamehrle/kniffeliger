package client.networking;

import client.gui.LobbyWindowController;

import client.Print;
import client.Client;
import client.gui.Main;
import server.Lobby;
import server.networking.CommandsServerToClient;

/**
 * This class handles the input read by the ClientInput class and processes it accordingly.
 */

public class ClientInputHelper implements Runnable {
    Client gameManager;
    String message;
    Pong pong;
    ClientOutput clientOutput;

    /**
     * The constructor for the ClientInputHelper
     * @param gameManager
     * @param message the input that was read by the ClientInput coming from the server
     */
    public ClientInputHelper(Client gameManager, String message) {
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
            case BRCT -> System.out.println("Alfred: " + input[1]);
            case LOLI -> {
                Print.printLobbies(input[1]);
                Main.lobbyList(input[1]);
            }
            case DICE -> System.out.println("Your dice are: " + input[1]);
            case SHES -> Print.printEntrySheet(input[1]);
            case SHAC -> Print.printActions(input[1]);
            case CRLO -> {
                Main.addNewLobby(input[1]);
                System.out.println("Lobby was send to the gui");
            }
            case ENLO -> Main.addNewPlayer(input[1]);
            case LELO -> {
                Main.removePlayer(input[1]);
                System.out.println("LELO received with message: " + input[1]);
            }
            default -> System.out.println("unknown command received from server " + message);
        }

    }
}
