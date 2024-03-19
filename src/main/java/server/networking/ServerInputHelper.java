package server.networking;

import client.networking.CommandsClientToServer;

public class ServerInputHelper implements Runnable {

    ClientThread client;
    String message;
    Ping ping;
    ServerOutput serverOutput;

    ServerInputHelper(ClientThread client, String message) {
        this.client = client;
        this.message = message;
        this.ping = client.getPing();
        this.serverOutput = client.getServerOutput();
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

            case CHNA -> client.changePlayerName(input[1]);
            case QUIT -> client.disconnect();
            case PONG -> ping.updatePong(input[1]);
            case PING -> serverOutput.send(CommandsServerToClient.PONG, input[1]);
            //TODO chat? eigene klasse?
            default -> System.out.println("unknown command received from client " + message);
        }




    }
}
