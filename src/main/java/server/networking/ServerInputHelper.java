package server.networking;

import client.networking.CommandsClientToServer;
import server.Player;

public class ServerInputHelper implements Runnable {

    ClientThread clientThread;
    String message;
    Ping ping;
    ServerOutput serverOutput;
    Player player;

    ServerInputHelper(ClientThread clientThread, String message) {
        this.clientThread = clientThread;
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

        switch (cmd) {

            case CHNA -> player.changePlayerName(input[1]);
            case QUIT -> clientThread.disconnect();
            case PONG -> ping.updatePong(input[1]);
            case PING -> serverOutput.send(CommandsServerToClient.PONG, input[1]);
            case CHAT -> Communication.sendChat(player, input[1]);
            case WHSP -> Communication.sendWhisper(player, input[1]);
            default -> System.out.println("unknown command received from client " + message);

        }
    }
}
