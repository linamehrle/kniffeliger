package server.networking;

import client.networking.CommandsClientToServer;
import org.apache.logging.log4j.Logger;
import server.HighScore;
import server.ListManager;
import server.Player;
import starter.Starter;

/**
 * This class handles the input read by the ServerInput class and processes it accordingly.
 */
public class ServerInputHelper implements Runnable {

    private Logger logger = Starter.getLogger();

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

        try {
            cmd = CommandsClientToServer.valueOf(input[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.info("Received invalid command " + input[0]);
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
            case CRLO -> ListManager.createNewLobby(player, input[1]);
            case ENLO -> player.enterLobby(input[1]);
            case LELO -> player.leaveLobby();
            case LOCH -> {
                Communication.sendToLobby(CommandsServerToClient.CHAT, player, input[1]);
                logger.debug("lobby chat received");
            }
            case STRT -> player.getLobby().startGame(player);
            case GAME -> player.getLobby().getGameManager().getAnswer(input[1]);
            case PLLI -> Communication.sendToPlayer(CommandsServerToClient.PLLI, player, ListManager.getPlayerListAsString());
            case HGSC -> Communication.sendToPlayer(CommandsServerToClient.HGSC, player, HighScore.getHighScoreList());
            default -> logger.info("unknown command received from client " + message);

        }
    }
}
