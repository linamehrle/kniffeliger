package server.networking;

import client.networking.CommandsClientToServer;
import server.ListManager;
import server.Player;
import server.gamelogic.GameManager;

/**
 * This class handles the input read by the ServerInput class and processes it accordingly.
 */
public class ServerInputHelper implements Runnable {

    ClientThread clientThread;
    String message;
    Ping ping;
    ServerOutput serverOutput;
    Player player;

    /**
     * The constructor for the ServerInputHelper
     * @param clientThread
     * @param message the input read by the ServerInput coming from the client
     */
    ServerInputHelper(ClientThread clientThread, String message) {
        this.clientThread = clientThread;
        this.message = message;
        this.ping = clientThread.getPing();
        this.serverOutput = clientThread.getServerOutput();
        this.player = clientThread.getPlayer();
    }

    /**
     * The run method for the ServerInputHelper. It splits the incoming string according to the network protocol
     * and handles the different cases accordingly.
     */
    @Override
    public void run() {

        String[] input = message.split(" ", 2);
        CommandsClientToServer cmd;

        /*if (input.length != 2) {
            System.out.println("Invalid message to server");
            serverOutput.send(CommandsServerToClient.BRCT, "Invalid message: try again.");
            return;
        }*/

        try {
            cmd = CommandsClientToServer.valueOf(input[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Received invalid command " + input[0]);
            serverOutput.send(CommandsServerToClient.BRCT, "Invalid command: try again.");
            return;
        }

        //switch case for the different possible incoming commands
        switch (cmd) {

            case CHNA -> player.changePlayerName(input[1]);
            case QUIT -> clientThread.disconnect();
            case PONG -> ping.updatePong(input[1]);
            case PING -> serverOutput.send(CommandsServerToClient.PONG, input[1]);
            case CHAT -> Communication.sendChat(player, input[1]);
            case WHSP -> Communication.sendWhisper(player, input[1]);
            case LOLI -> serverOutput.send(CommandsServerToClient.LOLI, ListManager.returnLobbyListAsString());
            case CRLO -> {
                ListManager.createNewLobby(player, input[1]);
                Communication.sendToGui(CommandsServerToClient.CRLO, input[1], player);
            }
            case ENLO -> player.enterLobby(input[1]);
            case LELO -> player.leaveLobby();
            case LOCH -> Communication.sendToLobby(player, input[1]);
            case STRT -> player.getLobby().startGame(player);
            //case ROLL -> serverOutput.send(CommandsServerToClient.DICE, GameManager.stringsAndRockNRoll()); method does not work
            case SAVE -> {
                GameManager.saveDice(input[1]);
                serverOutput.send(CommandsServerToClient.BRCT, "Dice successfully saved");
            }

            //case SHES -> serverOutput.send(CommandsServerToClient.SHES, function to get entry sheet as string by username);
            //function by anisja
            //if (input[1].equals(myOwnSheet)) { print the own sheet of the player }

            //case SHAC -> serverOutput.send(CommandsServerToClient.SHAC, function to get actions as one string);
            //function by anisja or in the player?

            //case PLAC -> give the action to the game manager, handles by anisja

            //case ENCO -> give the comb name to the game manager to enter the dice in the entry sheet, handled by anisja
            default -> System.out.println("unknown command received from client " + message);

        }
    }
}
