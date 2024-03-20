package server.networking;

import client.networking.CommandsClientToServer;
import server.Player;

public class ServerInputHelper implements Runnable {

    //ClientThread client;  not necc?
    String message;
    Ping ping;
    ServerOutput serverOutput;
    Player player;

    //wie greife ich auf die player liste auf dem server zu?

    ServerInputHelper(ClientThread clientThread, String message) {
        this.message = message;
        this.ping = clientThread.getPing();
        this.serverOutput = clientThread.getServerOutput();
        this.player = clientThread.getPlayer();
    }

    @Override
    public void run() {

        String[] input = message.split(" ", 2);
        CommandsClientToServer cmd;

        if (input.length != 2) {
            System.out.println("Alfred: invalid message to server");
            return;
        }

        try {
            cmd = CommandsClientToServer.valueOf(input[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Alfred: received invalid command " + input[0]);
            return;
        }

        switch (cmd) {

            case CHNA -> player.changePlayerName(input[1]);
            //case QUIT -> client.disconnect(); how to handle disconnect?
            case PONG -> ping.updatePong(input[1]);
            case PING -> serverOutput.send(CommandsServerToClient.PONG, input[1]);
            default -> System.out.println("unknown command received from client " + message);
        }




    }
}
