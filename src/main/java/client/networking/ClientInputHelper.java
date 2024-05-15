package client.networking;

import client.Print;
import client.Client;
import client.gui.Main;
import client.gui.SceneController;
import org.apache.logging.log4j.Logger;
import server.networking.CommandsServerToClient;
import starter.Starter;

/**
 * This class handles the input read by the ClientInput class and processes it accordingly.
 */

public class ClientInputHelper implements Runnable {
    private Logger logger = Starter.getLogger();
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
            logger.info("Client: received invalid command " + input[0]);
            logger.error(e.getMessage());
            return;
        }

        //switch case for the different possible incoming commands
        switch (cmd) {
            case QUIT -> gameManager.disconnect();
            case PING -> clientOutput.send(CommandsClientToServer.PONG, input[1]);
            case PONG -> pong.updatePong(input[1]);
            case CHAT -> Main.sendToChatWindow(input[1]);
            case BRCT -> {
                logger.info("Alfred: " + input[1]);
                Main.displayInGameWindow(input[1]);
            }
            case LOLI -> Main.lobbyList(input[1]);
            case CRLO -> {
                Main.addNewLobby(input[1]);
                logger.debug("Lobby was send to the gui");
            }
            case ENLO -> Main.addNewPlayer(input[1]);
            case LELO -> {
                Main.removePlayer(input[1]);
                logger.debug("LELO received with message: " + input[1]);
            }
            case ROLL -> {
                logger.debug("client input: dice received from server " + input[1]);
                Main.sendDiceToGUI(input[1]);
            }
            case PLLI -> Main.updateChatPlayerList(input[1]);
            case HGSC -> {
                //System.out.println(input[1]);
                Main.updateHighScore(input[1]);
            }
            case LOST -> Main.updateLobby(input[1]);
            // TODO: FIX LIFE
            case LOPL -> Main.updateGamePlayerList(input[1]);
            case RANK -> {
                SceneController.showWinnerWindow(input[1]);
                Main.sendEndOfGame();
            }
            case ACTN -> {
                Main.updateActionDice(input[1]);
                logger.debug("Action dice update received at the client: " + input[1]);
            }
            case ENTY -> {
                logger.debug("Client received command ENTY with message: " + input[1]);
                Main.updateEntrySheet(input[1]);
            }
            case ALDI -> Main.updateOtherDiceBox(input[1]);
            case STRT -> {
                logger.debug("received new message with command STRT: " + input[1]);
                Main.changeTurn(input[1]);
            }
            case TUSR -> Main.sendOwnNameToGUI(input[1]);
            case PONT -> Main.updateTotalScore(input[1]);
            case FRZE -> Main.freezeOrDefreeze(input[1]);
            case STRG -> Main.initGame();
            case SAVE -> {
                logger.debug("Received command SAVE with message: " + input[1]);
                Main.communicateSave(input[1]);
            }
            case ENDT -> Main.endTurn();
            default -> logger.info("unknown command received from server " + message);
        }
    }
}
