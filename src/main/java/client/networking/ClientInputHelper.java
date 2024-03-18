package client.networking;

import java.io.BufferedReader;
import java.io.IOException;

import client.manager.GameManager;
import server.networking.CommandsServerToClient;

public class ClientInputHelper implements Runnable {
    GameManager gameManager;
    String message;
    Pong pong;

    public ClientInputHelper(GameManager gameManager, String message) {
        this.gameManager = gameManager;
        this.message = message;
        this.pong = gameManager.getPong();
    }

    @Override
    public void run() {

        String[] input = message.split(" ", 2);
        CommandsServerToClient cmd;

        try {
            cmd = CommandsServerToClient.valueOf(input[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Client: received invalid command " + input[0]);
            return;
        }

        switch (cmd) {

            case CHNA -> System.out.println("Your username is now " + input[1] + ". To change it, use the command CHNA.");
            case QUIT -> gameManager.disconnect();
            case PING -> pong.returnPing(input[1]);
            default -> System.out.println("unknown command received from server " + message);
        }

    }
}
