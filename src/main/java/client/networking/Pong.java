package client.networking;

import client.manager.GameManager;

public class Pong implements Runnable {

    //private boolean receivedFirstPing = false;

    //stop überall direkt als false?
    private boolean stop = false;
    private long lastReceivedPong;
    private ClientOutput clientOutput;
    private GameManager gameManager;

    public Pong(GameManager gameManager) {
        this.gameManager = gameManager;
        this.clientOutput = gameManager.getClientOutput();
    }

    @Override
    public void run() {

        lastReceivedPong = System.currentTimeMillis();

        //checking for timeouts
        while (!stop && Math.abs(lastReceivedPong - System.currentTimeMillis()) < 5000) {
            clientOutput.send(CommandsClientToServer.PING, String.valueOf(System.currentTimeMillis()));
            //System.out.println("Ping send");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!stop) {
            System.out.println("Server can't be reached, client will be disconnected.");
            gameManager.disconnect();
        }
    }

    public void updatePong(String pongTime) {
        lastReceivedPong = Long.parseLong(pongTime);
        //System.out.println("Pong received and updated");
    }
    public void stop() {
        stop = true;
    }
}
