package client.networking;

import client.manager.GameManager;

public class Pong implements Runnable {

    private boolean receivedFirstPing = false;

    //stop überall direkt als false?
    private boolean stop = false;
    private long lastReceivedPing = System.currentTimeMillis();
    private ClientOutput clientOutput;
    private GameManager gameManager;

    public Pong(GameManager gameManager) {
        this.gameManager = gameManager;
        this.clientOutput = gameManager.getClientOutput();
    }

    @Override
    public void run() {
        //waiting for the first ping
        while (!receivedFirstPing && !stop) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //check difference in system times server/client
        long timeDifference = Math.abs(lastReceivedPing - System.currentTimeMillis());

        //checking for timeouts
        while (!stop && Math.abs(lastReceivedPing - System.currentTimeMillis()) < timeDifference + 5000) {
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

    public void returnPing(String pingTime) {
        if (!receivedFirstPing) {
            receivedFirstPing = true;
        }
        lastReceivedPing = Long.parseLong(pingTime);
        clientOutput.send(CommandsClientToServer.PONG, pingTime);
        //System.out.println("Ping received and returned");
    }

    public void stop() {
        stop = true;
    }
}
