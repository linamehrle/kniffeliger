package server.networking;

import client.networking.CommandsClientToServer;
import server.Lobby;
import server.Player;
import server.Server;

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

        if (input.length != 2) {
            System.out.println("Invalid message to server");
            serverOutput.send(CommandsServerToClient.BRCT, "Invalid message: try again.");
            return;
        }

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
            case LOLI -> serverOutput.send(CommandsServerToClient.LOLI, Server.returnLobbyList());
            case CRLO -> {
                if (Server.lobbyNameIsTaken(input[1])) {
                    serverOutput.send(CommandsServerToClient.BRCT, "Name is already taken");
                } else {
                    Server.getLobbyList().add(new Lobby(input[1]));
                    System.out.println("Player " + player.getUsername() + " created a new lobby: " + input[1]);
                    //TODO broadcast that a new lobby has been created?
                }
            }
            case ENLO -> {
                if(Server.lobbyExists(input[1])) {
                    Server.getLobbyFromList(input[1]).enterLobby(player);
                } else {
                    serverOutput.send(CommandsServerToClient.BRCT, "There is no lobby with this name");
                }
            }
            default -> System.out.println("unknown command received from client " + message);

        }
    }
}
