package server.networking;

import server.Player;

/**
 * This class handles the thread which sends a ping to the client in set intervals. It also handles a timeout.
 */
public class Ping implements Runnable{

    /**
     * This variable indicates whether the thread is running. It will be set true when the client disconnects.
     */
    private boolean stop = false;
    private ClientThread clientThread;
    private Player player; //change to only get player name?
    private ServerOutput serverOutput;
    private long lastReceivedPong;

    /**
     * The constructor for Ping
     * @param clientThread
     */
    public Ping(ClientThread clientThread) {
        this.clientThread = clientThread;
        this.serverOutput = clientThread.getServerOutput();
        this.player = clientThread.getPlayer();
    }

    /**
     * The run method for Ping. It sends pings to the client every two seconds and shuts down this separate player object
     * when a timeout is detected.
     */
    @Override
    public void run() {

        lastReceivedPong = System.currentTimeMillis();

        while (!stop && Math.abs(System.currentTimeMillis() - lastReceivedPong) < 5000) {
            serverOutput.send(CommandsServerToClient.PING, String.valueOf(System.currentTimeMillis()));
            //System.out.println("Ping send");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // this occurs if a timeout is detected
        if (!stop) {
           System.out.println(player.getUsername() + " has timed out");
           clientThread.disconnect();
        }

    }

    /**
     * this method updates the time of the last received returned ping from the client
     * @param pongTime
     */
    public void updatePong(String pongTime) {
        lastReceivedPong = Long.parseLong(pongTime);
        //System.out.println("Pong received and updated");
    }

    /**
     * This method sets the variable stop as true. It is used to handle a disconnect.
     */
    public void stop() {
        stop = true;
    }
}
