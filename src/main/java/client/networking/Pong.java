package client.networking;

import client.Client;
import org.apache.logging.log4j.Logger;
import starter.Starter;

/**
 * This class handles the thread which sends a ping to the server in set intervals. It also handles a timeout.
 */
public class Pong implements Runnable {
    Logger logger = Starter.getLogger();

    /**
     * This variable indicates whether the thread is running. It will be set true when the client disconnects.
     */
    private boolean stop = false;

    /**
     * This variable saves the time of the last received ping from the server.
     */
    private long lastReceivedPing;
    private ClientOutput clientOutput;
    private Client gameManager;

    /**
     * The constructor for Pong.
     * @param gameManager
     */
    public Pong(Client gameManager) {
        this.gameManager = gameManager;
        this.clientOutput = gameManager.getClientOutput();
    }

    /**
     * The run method for Pong. It sends pings to the server every two seconds and shuts down the client when a timeout
     * is detected.
     */
    @Override
    public void run() {

        lastReceivedPing = System.currentTimeMillis();

        while (!stop && Math.abs(lastReceivedPing - System.currentTimeMillis()) < 5000) {
            clientOutput.send(CommandsClientToServer.PING, String.valueOf(System.currentTimeMillis()));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }

        //this occurs if a timeout is detected
        if(!stop) {
            logger.info("Server can't be reached, shutting down now.");
            gameManager.disconnect();
        }
    }

    /**
     * this method updates the time of the last received returned ping from the server
     * @param pongTime
     */
    public void updatePong(String pongTime) {
        lastReceivedPing = Long.parseLong(pongTime);
    }

    /**
     * This method sets the variable stop as true. It is used to handle a disconnect.
     */
    public void stop() {
        stop = true;
    }
}
