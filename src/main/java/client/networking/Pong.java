package client.networking;

import client.manager.GameManager;

/**
 * This class handles the thread which sends a ping to the server in set intervals. It also handles a timeout.
 */
public class Pong implements Runnable {

    /**
     * This variable indicates whether the thread is running. It will be set true when the client disconnects.
     */
    private boolean stop = false;

    /**
     * This variable saves the time of the last received ping from the server.
     */
    private long lastReceivedPing;
    private ClientOutput clientOutput;
    private GameManager gameManager;

    /**
     * The constructor for Pong.
     * @param gameManager
     */
    public Pong(GameManager gameManager) {
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
            //System.out.println("Ping send");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //this occurs if a timeout is detected
        if(!stop) {
            System.out.println("Server can't be reached, shutting down now.");
            gameManager.disconnect();
        }
    }

    /**
     * this method updates the time of the last received returned ping from the client
     * @param pongTime
     */
    public void updatePong(String pongTime) {
        lastReceivedPing = Long.parseLong(pongTime);
        //System.out.println("Pong received and updated");
    }

    /**
     * This method sets the variable stop as true. It is used to handle a disconnect.
     */
    public void stop() {
        stop = true;
    }
}
