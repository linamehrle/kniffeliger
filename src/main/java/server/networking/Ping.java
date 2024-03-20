package server.networking;

import server.Player;

public class Ping implements Runnable{
    private boolean stop = false;
    private ClientThread clientThread;
    private Player player; //change to only get player name?
    private ServerOutput serverOutput;
    private long lastReceivedPong;

    public Ping(ClientThread clientThread) {
        this.clientThread = clientThread;
        this.serverOutput = clientThread.getServerOutput();
        this.player = clientThread.getPlayer();
    }


    //are the time limits okay?
    @Override
    public void run() {

        lastReceivedPong = System.currentTimeMillis();

        while (!stop && Math.abs(System.currentTimeMillis() - lastReceivedPong) < 5000) {
            serverOutput.send(CommandsServerToClient.PING, String.valueOf(System.currentTimeMillis()));
            System.out.println("Ping send");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!stop) {
           System.out.println(player.getUsername() + " has timed out.");
           clientThread.disconnect();
        }

    }

    public void updatePong(String pongTime) {
        //does this need an exception?
        lastReceivedPong = Long.parseLong(pongTime);
        System.out.println("Pong received and updated");
    }
    public void stop() {
        stop = true;
    }
}
