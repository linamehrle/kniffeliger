package server.networking;

import client.networking.CommandsClientToServer;

public class ServerInputHelper implements Runnable {

    ClientThread client;
    String message;

    ServerInputHelper(ClientThread client, String message) {
        this.client = client;
        this.message = message;
    }

    @Override
    public void run() {

        String[] input = message.split(" ", 2);
        CommandsClientToServer cmd;

        try {
            cmd = CommandsClientToServer.valueOf(input[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Alfred: received invalid command " + input[0]);
            return;
        }

        switch (cmd) {

            case CHNA -> client.changePlayerName(input[1]);
            default -> System.out.println("unknown command received from client " + message);
        }




    }
}
